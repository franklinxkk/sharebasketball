package com.bblanqiu.mqtt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bblanqiu.module.mqtt.almq.MQTTRecvMsg;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/root-context.xml", "classpath:spring/servlet-context.xml" })   
public class MQTTTest {
	@Autowired
	MQTTRecvMsg mqttRecvMsg;
	@Test
	public void test(){
		mqttRecvMsg.recvMsg();
	}
}
