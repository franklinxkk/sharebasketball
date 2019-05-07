package com.bblanqiu.module.mqtt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bblanqiu.amqp.AmqpMessageSender;
import com.bblanqiu.common.bean.OnlyResultBean;
import com.bblanqiu.common.exception.HandleExceptionController;
import com.bblanqiu.common.mysql.model.DeviceAlarm;
import com.bblanqiu.common.mysql.model.StorageBox;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.device.service.DeviceService;
import com.bblanqiu.module.mqtt.almq.MQTTSendMsg;
import com.bblanqiu.module.mqtt.converter.DataConverter;

@Controller
public class MqttController extends HandleExceptionController{
	@Autowired
	MQTTSendMsg mqttSendMsg;
	@Autowired
	DeviceService deviceService;
	@Autowired
	AmqpMessageSender sender;

	@RequestMapping(value = "/mqtt/init", method = RequestMethod.GET)
	public @ResponseBody Object getEmailCode(
			@RequestParam(required = true) String sn,
			@RequestParam(required = true) String auth
			){
		if(auth.equals("likang")){
			mqttSendMsg.sendMsg(sn, DataConverter.warpDeviceInitCmd(sn));
		}
		return new OnlyResultBean("ok");
	}
	@RequestMapping(value = "/mqtt/openballbox", method = RequestMethod.GET)
	public @ResponseBody Object openball(
			@RequestParam(required = true) String sn,
			@RequestParam(required = true) String auth
			){
		if(auth.equals("likang")){
			mqttSendMsg.sendMsg(sn, DataConverter.warpOpenBallBox(sn));
		}
		return new OnlyResultBean("ok");
	}
	@RequestMapping(value = "/mqtt/openstoragebox", method = RequestMethod.GET)
	public @ResponseBody Object openstoragebox(
			@RequestParam(required = true) String sn,
			@RequestParam(required = true) String auth,
			@RequestParam(required = false) Integer num
			)throws Exception{
		if(auth.equals("likang")){
			StorageBox sb = deviceService.getStorageboxByDeviceSnAndNum(sn, num);
			sb.setState(1);
			deviceService.updateStorageboxById(sb);
			mqttSendMsg.sendMsg(sn, DataConverter.warpOpenStorageBox(sn, num));
		}
		return new OnlyResultBean("ok");
	}
	@RequestMapping(value = "/mqtt/ack", method = RequestMethod.GET)
	public @ResponseBody Object ack(
			@RequestParam(required = true) String sn,
			@RequestParam(required = false, defaultValue="1") Integer state
			){
		mqttSendMsg.sendMsg(sn, DataConverter.warpAck(sn, state));
		return new OnlyResultBean("ok");
	}
	@RequestMapping(value = "/mqtt/alarmsender", method = RequestMethod.GET)
	public @ResponseBody Object alarmsender(
			@RequestParam(required = true) String sn,
			@RequestParam(required = true) Integer type
			){
		DeviceAlarm da = new DeviceAlarm();
		da.setSn(sn);
		da.setType(type);
		da.setAlarmTime(DateUtils.getDate());
		da.setDescription("test");
		sender.asyncPushDataMessage(da, "alarm");
		return new OnlyResultBean("ok");
	}
}
