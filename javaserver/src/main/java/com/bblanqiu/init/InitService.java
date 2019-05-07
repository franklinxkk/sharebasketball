package com.bblanqiu.init;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.module.mqtt.almq.MQTTRecvMsg;
import com.bblanqiu.module.mqtt.almq.MqttStateConsumer;

@Component
public class InitService {
	private static Logger logger = LoggerFactory.getLogger(InitService.class);
	@Autowired
	MQTTRecvMsg mqttRecvMsg;
	@Autowired 
	MqttStateConsumer mqttStateConsumer;
	@PostConstruct
	public void init() throws Exception{
		initcatche();
		startService();
	}
	
	@PreDestroy
	public void destory(){
		mqttRecvMsg.stop();
		mqttStateConsumer.stop();
	}
	
	/**
	 * ��ʼ������
	 */
	private void initcatche(){
		try {
		} catch (Exception e) {
			logger.error("error to init device key cache:{}", e.getMessage());
			e.printStackTrace();
		}
	}
	private void initPay(){
		 //TODO:
	}
	/**
	 * ������פ����
	 */
	private void startService() {
		try {
			mqttRecvMsg.recvMsg();
			mqttStateConsumer.consumMqttStateMsg();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
}
