package com.bblanqiu.module.mqtt.almq;

import javax.net.SocketFactory;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MQTTSendMsg {
	private static Logger logger = LoggerFactory.getLogger(MQTTSendMsg.class);
	private MqttClient sampleClient;
	private MqttConnectOptions connOpts;
	public MQTTSendMsg(){
		MemoryPersistence persistence = new MemoryPersistence();
        try {
            sampleClient = new MqttClient(AlmqConstant.sslbroker, AlmqConstant.clientIdS, persistence);
            connOpts = new MqttConnectOptions();
            logger.info("Sender Connecting to broker: " + AlmqConstant.sslbroker);
            /**
             * 计算签名，将签名作为MQTT的password。
             * 签名的计算方法，参考工具类MacSignature，第一个参数是ClientID的前半部分，即GroupID
             * 第二个参数阿里云的SecretKey
             */
            String sign = MacSignature.macSignature(AlmqConstant.clientIdS.split("@@@")[0], AlmqConstant.secretKey);
            connOpts.setUserName(AlmqConstant.acessKey);
            connOpts.setServerURIs(new String[] { AlmqConstant.sslbroker });
            connOpts.setPassword(sign.toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(90);
            SocketFactory socketFactory = MqttSSLConfig.initSSLSocket();
			connOpts.setSocketFactory(socketFactory);
            sampleClient.setCallback(new MqttCallback() {
                public void connectionLost(Throwable throwable) {
                    System.out.println("mqtt connection lost");
                    while(!sampleClient.isConnected()){
                        try {
                            Thread.sleep(100);
                            sampleClient.connect(connOpts);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                    System.out.println(" sender messageArrived:" + topic + "------" + new String(mqttMessage.getPayload()));
                }
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    System.out.println("sender deliveryComplete:" + iMqttDeliveryToken.getMessageId());
                }
            });
        } catch (Exception me) {
            me.printStackTrace();
        }
	}
	public void sendMsg(String sn, byte[] cmd){
        try {
        	if(!sampleClient.isConnected()){
        		sampleClient.connect(connOpts);
        		logger.info("mqtt-sender-reconnect");
        	}
            try {
                final MqttMessage message = new MqttMessage(cmd);
                message.setQos(2);
                logger.info("mqtt-pushed-at:"+sn+" "+MqttOpenTool.getPayload(cmd));
                sampleClient.publish(AlmqConstant.topic+"/"+sn+"/cmd", message);//topic+"/+/data",topic+"/+/data/p2p"
            } catch (Exception e) {
                e.printStackTrace();
            }    
        } catch (Exception me) {
            me.printStackTrace();
        }
	}
	public void sendMsgToDataChannle(String sn, byte[] data){
        try {
        	if(!sampleClient.isConnected()){
        		sampleClient.connect(connOpts);
        		logger.info("mqtt sender recon");
        	}
            try {
                final MqttMessage message = new MqttMessage(data);
                message.setQos(2);
                logger.info("mqtt pushed at:"+sn+" "+MqttOpenTool.getPayload(data));
                sampleClient.publish(AlmqConstant.topic+"/"+sn+"/data", message);//topic+"/+/data",topic+"/+/data/p2p"
            } catch (Exception e) {
                e.printStackTrace();
            }    
        } catch (Exception me) {
            me.printStackTrace();
        }
	}
}
