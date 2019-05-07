package com.bblanqiu.module.mqtt.almq;

import java.io.IOException;

import javax.net.SocketFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.amqp.AmqpMessageSender;
import com.bblanqiu.common.mysql.model.DeviceAlarm;
import com.bblanqiu.module.mqtt.bean.DeviceInitBean;
import com.bblanqiu.module.mqtt.bean.OpenLockBean;
import com.bblanqiu.module.mqtt.bean.OtherDataBean;
import com.bblanqiu.module.mqtt.converter.DataConverter;

@Component
public class MQTTRecvMsg {
	private static Logger logger = LoggerFactory.getLogger(MQTTRecvMsg.class);
	@Autowired
	AmqpMessageSender mqpSender;
	private MqttClient sampleClient;
	private boolean droped = false;
	public MQTTRecvMsg(){
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			sampleClient = new MqttClient(AlmqConstant.sslbroker, AlmqConstant.clientIdR, persistence);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	public void stop(){
		try {
			droped = true;
			sampleClient.close();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	public void recvMsg(){
		try{
			final MqttConnectOptions connOpts = new MqttConnectOptions();
			logger.info("Recv Connecting to sslbroker: " + AlmqConstant.sslbroker);
			/**
			 * 计算签名，将签名作为MQTT的password
			 * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
			 * 第二个参数阿里云的SecretKey
			 */
			final String sign = MacSignature.macSignature(AlmqConstant.clientIdR.split("@@@")[0], AlmqConstant.secretKey);
			/**
			 * 设置订阅方订阅的Topic集合，此处遵循MQTT的订阅规则，可以是一级Topic，二级Topic，P2P消息请订阅/p2p
			 */
			final String[] topicFilters=new String[]{AlmqConstant.topic+"/+/data",AlmqConstant.topic+"/p2p"};
			final int[]qos={0,0};
			connOpts.setUserName(AlmqConstant.acessKey);
			connOpts.setServerURIs(new String[] { AlmqConstant.sslbroker });
			connOpts.setPassword(sign.toCharArray());
			connOpts.setCleanSession(true);
			connOpts.setKeepAliveInterval(90);
			/**
			 *导入证书的路径，生成加密连接
			 */
			SocketFactory socketFactory = MqttSSLConfig.initSSLSocket();
			connOpts.setSocketFactory(socketFactory);
			sampleClient.setCallback(new MqttCallback() {
				public void connectionLost(Throwable throwable) {
					logger.info("mqtt connection lost");
					throwable.printStackTrace();
					while(!sampleClient.isConnected() && !droped){
						try {
							Thread.sleep(1000);
							sampleClient.connect(connOpts);
							//客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
							sampleClient.subscribe(topicFilters,qos);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
					logger.info("messageArrived:" + topic + "------" + MqttOpenTool.getPayload(mqttMessage.getPayload()));
					byte[] pac = mqttMessage.getPayload();
					int header = (byte)0xbb;
					if(header == pac[0]){
						int type = pac[1];
						if(type == 1){//数据报文
							DeviceInitBean dib = DataConverter.unWarpDeviceInitData(pac);
							mqpSender.asyncPushDataMessage(dib, String.valueOf(type));
							logger.info("get type:{},data:{}",type,dib);
						}else if(type == 2){
							DeviceInitBean dib = DataConverter.unWarpDeviceData(pac);
							mqpSender.asyncPushDataMessage(dib, String.valueOf(type));
							logger.info("get type:{},data:{}",type,dib);
						}else if(type == 3){
							OtherDataBean odb = DataConverter.unwarpOtherData(pac);
							mqpSender.asyncPushDataMessage(odb, String.valueOf(type));
							logger.info("get type:{},data:{}",type,odb);
						}else if(type == 4){
							OtherDataBean odb = DataConverter.unwarpOtherData(pac);
							mqpSender.asyncPushDataMessage(odb, String.valueOf(type));
							logger.info("get type:{},data:{}",type,odb);
						}else if(type == 5){
							OtherDataBean odb = DataConverter.unwarpOtherData(pac);
							mqpSender.asyncPushDataMessage(odb, String.valueOf(type));
							logger.info("get type:{},data:{}",type,odb);
						}else if(type < 32){
							
							
						}else if(type < 80){//告警报文
							DeviceAlarm da = DataConverter.unwarpAlarmData(pac);
							mqpSender.asyncPushDataMessage(da, "alarm");
							logger.info("get type:{},alarm:{}",type);
						}else if(type == 113){//开篮球箱成功
							OpenLockBean olb = DataConverter.unwarpLockBallData(pac);
							mqpSender.asyncPushDataMessage(olb, String.valueOf(type));
							logger.info("get type:{},data:{}",type, olb);
						}else if(type == 114){//开储物柜成功
							OpenLockBean olb = DataConverter.unwarpLockBoxData(pac);
							mqpSender.asyncPushDataMessage(olb, String.valueOf(type));
							logger.info("get type:{},data:{}",type, olb);
						}else{
							
						}
					}else{
						logger.info("unknown pac header:{}",header);
					}
				}
				public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
					logger.info("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
				}
			});
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(!droped){
							//客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
							sampleClient.connect(connOpts);
							//每个客户端最多允许存在30个订阅关系，超出限制可能会丢弃导致收不到部分消息
							sampleClient.subscribe(topicFilters,qos);
							Thread.sleep(Integer.MAX_VALUE);
						}
					} catch (Exception me) {
						me.printStackTrace();
						
					}
				}
			});
			t.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws IOException {
	        /**
	         * MQTT的ClientID，一般由两部分组成，GroupID@@@DeviceID
	         * 其中GroupID在MQ控制台里申请
	         * DeviceID由应用方设置，可能是设备编号等，需要唯一，否则服务端拒绝重复的ClientID连接
	         */
	        final String clientId ="GID_BBLQ_DEVICE@@@franklintest";
	        String sign;
	        MemoryPersistence persistence = new MemoryPersistence();
	        try {
	            final MqttClient sampleClient = new MqttClient(AlmqConstant.broker, clientId, persistence);
	            final MqttConnectOptions connOpts = new MqttConnectOptions();
	            System.out.println("Connecting to broker: " + AlmqConstant.broker);
	            /**
	             * 计算签名，将签名作为MQTT的password
	             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
	             * 第二个参数阿里云的SecretKey
	             */
	            sign = MacSignature.macSignature(clientId.split("@@@")[0], AlmqConstant.secretKey);
	            /**
	             * 设置订阅方订阅的Topic集合，此处遵循MQTT的订阅规则，可以是一级Topic，二级Topic，P2P消息请订阅/p2p
	             */
	            final String[] topicFilters=new String[]{AlmqConstant.topic+"/+/data",AlmqConstant.topic+"/p2p"};
	            final int[]qos={2,0};
	            connOpts.setUserName(AlmqConstant.acessKey);
	            connOpts.setServerURIs(new String[] { AlmqConstant.broker });
	            connOpts.setPassword(sign.toCharArray());
	            connOpts.setCleanSession(true);
	            connOpts.setKeepAliveInterval(90);
	            sampleClient.setCallback(new MqttCallback() {
	                public void connectionLost(Throwable throwable) {
	                    System.out.println("mqtt connection lost");
	                    throwable.printStackTrace();
	                    while(!sampleClient.isConnected()){
	                        try {
	                            Thread.sleep(1000);
	                            sampleClient.connect(connOpts);
	                            //客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
	                            sampleClient.subscribe(topicFilters,qos);
	                        } catch (Exception e) {
	                            e.printStackTrace();
	                        }
	                    }
	                }
	                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
	                    System.out.println("messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
	                    int header = (byte)0xbb;
	                    byte[] pac = mqttMessage.getPayload();
						if(header == pac[0]){
							int type = pac[1];
							if(type == 1){//数据报文
								DeviceInitBean dib = DataConverter.unWarpDeviceInitData(pac);
								logger.info("get type:{},data:{}",type,dib);
							}else if(type == 2){
								DeviceInitBean dib = DataConverter.unWarpDeviceData(pac);
								logger.info("get type:{},data:{}",type,dib);
							}else if(type == 3){
								OtherDataBean odb = DataConverter.unwarpOtherData(pac);
								logger.info("get type:{},data:{}",type,odb);
							}else if(type == 4){
								OtherDataBean odb = DataConverter.unwarpOtherData(pac);
								logger.info("get type:{},data:{}",type,odb);
							}else if(type < 32){
								
							}else if(type < 80){//告警报文
								logger.info("get type:{},alarm:{}",type);
							}else{//命令
							
							}
						}
	                }
	                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
	                    System.out.println("deliveryComplete:" + iMqttDeliveryToken.getMessageId());
	                }
	            });
	            //客户端每次上线都必须上传自己所有涉及的订阅关系，否则可能会导致消息接收延迟
	            sampleClient.connect(connOpts);
	            //每个客户端最多允许存在30个订阅关系，超出限制可能会丢弃导致收不到部分消息
	            sampleClient.subscribe(topicFilters,qos);
	            Thread.sleep(Integer.MAX_VALUE);
	        } catch (Exception me) {
	            me.printStackTrace();
	        }
	    }

}
