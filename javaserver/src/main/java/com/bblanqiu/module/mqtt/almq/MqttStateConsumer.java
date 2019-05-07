package com.bblanqiu.module.mqtt.almq;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSONObject;
import com.bblanqiu.amqp.AmqpMessageSender;
import com.bblanqiu.module.mqtt.bean.OtherDataBean;
@Component
public class MqttStateConsumer {
	private static Logger logger = LoggerFactory.getLogger(MqttStateConsumer.class);
	@Autowired
	AmqpMessageSender mqpSender;
	private Consumer consumer;
	private boolean droped = false;
	public MqttStateConsumer(){
		Properties properties = new Properties();
		properties.put(PropertyKeyConst.ConsumerId, "CID_bblq_mqtt_consumer");
		properties.put(PropertyKeyConst.ONSAddr,
				"http://onsaddr-internal.aliyun.com:8080/rocketmq/nsaddr4client-internal");
		properties.put(PropertyKeyConst.AccessKey, AlmqConstant.acessKey);
		properties.put(PropertyKeyConst.SecretKey, AlmqConstant.secretKey);
		consumer = ONSFactory.createConsumer(properties);
	}

	public void stop(){
		droped = true;
		consumer.shutdown();
	}
	public void consumMqttStateMsg(){
		logger.info(" mqttclient state consumer started");
		consumer.subscribe("GID_BBLQ_DEVICE_MQTT", "*", new MessageListener() {
			public Action consume(Message message, ConsumeContext context) {
				//logger.info("Receive-mqtt-mq: " + message);
				try {
					OtherDataBean odb = new OtherDataBean();
					String event = message.getTag();
					if(event.equals("connect") || event.equals("disconnect") || event.equals("tcpclean")){
						String state = "";
						if(event.equals("connect")){
							// this is connect event
							state = "connect";
							odb.setState(1);
						}else if(event.equals("disconnect")){
							// this is client disconnect event
							state = "disconnect";
							odb.setState(0);
						}else if(event.equals("tcpclean")){
							// this is client disconnect event
							state = "tcpclean";
							odb.setState(0);
						}
						String body = new String(message.getBody());
						JSONObject object = JSON.parseObject(body);
						String clientId = object.getString("clientId");
						

						if(clientId.contains("@@@")){
							String sn = clientId.split("@@@")[1];
							logger.info("mqtt client:{},state:{}",sn,state);
							if(sn.length() == 8){
								odb.setSn(sn);
								mqpSender.asyncPushDataMessage(odb, "state");
							} 
						}
					}else{
						logger.info("receice other msg:{}",message.toString());
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return Action.CommitMessage;
			}
		});
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					while(!droped){
						consumer.start();
						Thread.sleep(Integer.MAX_VALUE);
					}
				} catch (Exception me) {
					me.printStackTrace();

				}
			}
		});
		t.start();
	}
}
