package com.bblanqiu.amqp.handler;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.mysql.model.BasketBall;
import com.bblanqiu.common.mysql.model.BeforeOpenDevice;
import com.bblanqiu.common.mysql.model.Device;
import com.bblanqiu.common.mysql.model.DeviceAlarm;
import com.bblanqiu.common.mysql.model.DeviceData;
import com.bblanqiu.common.mysql.model.StorageBox;
import com.bblanqiu.common.mysql.model.User;
import com.bblanqiu.common.mysql.model.UserCost;
import com.bblanqiu.common.mysql.model.UserScore;
import com.bblanqiu.common.mysql.model.Wallet;
import com.bblanqiu.common.util.CostUtil;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.alarm.bean.AlarmType;
import com.bblanqiu.module.alarm.facade.AlarmFacade;
import com.bblanqiu.module.device.bean.DeviceTypeBean;
import com.bblanqiu.module.device.service.DeviceService;
import com.bblanqiu.module.mqtt.almq.MQTTSendMsg;
import com.bblanqiu.module.mqtt.almq.MqttOpenTool;
import com.bblanqiu.module.mqtt.bean.CmdTransBean;
import com.bblanqiu.module.mqtt.bean.DeviceInitBean;
import com.bblanqiu.module.mqtt.bean.OpenLockBean;
import com.bblanqiu.module.mqtt.bean.OtherDataBean;
import com.bblanqiu.module.mqtt.converter.DataConverter;
import com.bblanqiu.module.sms.SmsHander;
import com.bblanqiu.module.user.service.TradeService;
import com.bblanqiu.module.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
@Component
public class DataHandler implements MessageListener, ChannelAwareMessageListener{
	private static Logger logger = LoggerFactory.getLogger(DataHandler.class);
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private UserService userService;
	@Autowired
	private TradeService tradeService;
	@Autowired
	private MQTTSendMsg mqttSendMsg;
	@Autowired
	private AlarmFacade alarmFacade;
	@Override
	public void onMessage(Message message, Channel channel){
		final String routingkey = message.getMessageProperties().getReceivedRoutingKey();
		final byte[] body = message.getBody();
		synchronized (body) {
			try {
				handleMsg(routingkey, body);
			} catch (Exception e) {
				e.printStackTrace();
			}	
		}
	}
	@Override
	public void onMessage(Message message) {
		/*
		final String routingkey = message.getMessageProperties().getReceivedRoutingKey();
		final byte[] body = message.getBody();
		logger.info("routingkey:"+routingkey);
		try {
			handleMsg(routingkey, body);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}*/
	}
	public static void  main(String []args) throws IOException{
		String r = "bblq.key.data.113";
		System.out.println(r.endsWith("113"));
		String[] rks = r.split("\\.");
		System.out.println(rks[rks.length - 1]);
	}
	private void handleMsg(final String routingkey, final byte[] body)throws Exception{
		logger.info("routingkey:{},body:{}",routingkey,mapper.readValue(body, Map.class).toString());
		String[] rks = routingkey.split("\\.");
		String type = rks[rks.length -1];
		if(type.equals("1")){//球初始化到篮球箱
			final DeviceInitBean d = mapper.readValue(body, DeviceInitBean.class);
			DeviceData dd = new DeviceData(d.getDeviceSn(), null, null, d.getLongitude(), d.getLatitude(), 
					d.getImsi(), d.getStance(), d.getWet(), d.getTemp(), d.getPower(), d.getSignal(), DateUtils.getDate());
			dd.setLockState("222222222");
			Device device = new Device();
			device.setLockState(dd.getLockState());
			device.setSn(d.getDeviceSn());
			device.setBallSn(d.getBallSn());
			device.setImsi(d.getImsi());
			device.setSignalStrength(d.getSignal());
			device.setTemp(d.getTemp());
			device.setWet(d.getWet());
			device.setPower(d.getPower());
			device.setLatitude(d.getLatitude());
			device.setLongitude(d.getLongitude());
			device.setStance(d.getStance());
			device.setState(1);
			deviceService.updateDeviceBySN(device);
			deviceService.createDeviceData(dd);
		}if(type.equals("2")){//普通设备数据包
			final DeviceInitBean d = mapper.readValue(body, DeviceInitBean.class);
			DeviceData dd = new DeviceData(d.getDeviceSn(), null, null, d.getLongitude(), d.getLatitude(), 
					d.getImsi(), d.getStance(), d.getWet(), d.getTemp(), d.getPower(), d.getSignal(), DateUtils.getDate());
			dd.setLockState(d.getLockState());
			Device device = new Device();
			device.setLockState(dd.getLockState());
			device.setSn(d.getDeviceSn());
			device.setBallSn(d.getBallSn());
			device.setImsi(d.getImsi());
			device.setSignalStrength(d.getSignal());
			device.setTemp(d.getTemp());
			device.setWet(d.getWet());
			device.setPower(d.getPower());
			device.setLatitude(d.getLatitude());
			device.setLongitude(d.getLongitude());
			device.setStance(d.getStance());
			device.setState(1);
			deviceService.updateDeviceBySN(device);
			deviceService.createDeviceData(dd);
		}else if(type.equals("statistics")){
			UserCost uc = mapper.readValue(body, UserCost.class);
			/*if(uc.getState() == 1 && uc.getType() == 2){
						adminFacade.addBoxTrade(uc.getConsume());
					}*/
			UserScore us = new UserScore();
			us.setCreateTime(uc.getEndTime());
			us.setScore(1);
			us.setType(uc.getType());
			us.setUserId(uc.getUserId());
			tradeService.createUserScore(us);
		}else if(type.equals("3")){//还球
			final OtherDataBean d = mapper.readValue(body, OtherDataBean.class);
			UserCost uc = tradeService.getCurrentUserBallCost(d.getSn());
			if(uc != null){
				Device device = deviceService.getDeviceBySN(d.getSn());
				
				if(d.getBallSn() == null || device.getBallSn().equals(d.getBallSn())){
					uc.setEndTime(DateUtils.getDate());
					uc.setState(1);

					Wallet w = tradeService.getWallet(uc.getUserId());//0.5元 30分钟
					int minute = DateUtils.getTimeGapByMinute(uc.getStartTime(), uc.getEndTime());
					uc.setConsume(CostUtil.getCost(minute));
					w.setMoney(w.getMoney() - uc.getConsume());
					w.setScore(w.getScore() + 1);
					
					BasketBall bb = new BasketBall();
					bb.setSn(device.getBallSn());
					bb.setState(0);

					tradeService.closeBasketball(uc, bb, w);
					//adminFacade.addBallTrade(uc.getConsume());
					UserScore us = new UserScore();
					us.setCreateTime(uc.getEndTime());
					us.setScore(1);
					us.setType(uc.getType());
					us.setUserId(uc.getUserId());
					tradeService.createUserScore(us);
					mqttSendMsg.sendMsg(d.getSn(), DataConverter.warpAck(d.getSn(), 1));
				}else{
					mqttSendMsg.sendMsg(d.getSn(), DataConverter.warpAck(d.getSn(), 2));
				}
				
				
			}
		}else if(type.equals("4")){//操作储物箱
			final OtherDataBean d = mapper.readValue(body, OtherDataBean.class);
			User user = userService.getUserbyPhone(d.getPhone());
			if(user != null){
				logger.info("user:{}",user.toString());
				UserCost uc = tradeService.getCurrentUserBoxCost(user.getId());
				boolean use = false;
				if(uc != null){
					if(d.getPass().equals(user.getStoragepass())){
						//开储物箱
						mqttSendMsg.sendMsg(uc.getDeviceSn(), DataConverter.warpOpenStorageBox(uc.getDeviceSn(), uc.getNum()));
						use = true;
					}else{
						mqttSendMsg.sendMsg(uc.getDeviceSn(), DataConverter.warpAck(uc.getDeviceSn(), 2));
						logger.info("uc box open:pwd error");
					}
				}
				UserCost ballCost = tradeService.getCurrentUserBallCost(user.getId());
				if(ballCost != null){
					if(d.getPass().equals(user.getStoragepass())){
						//开篮球柜
						Thread.sleep(1000);
						mqttSendMsg.sendMsg(d.getSn(), DataConverter.warpOpenBallBox(d.getSn()));
						use = true;
					}else{
						mqttSendMsg.sendMsg(d.getSn(), DataConverter.warpAck(d.getSn(), 2));
						logger.info("uc ball open:pwd error");
					}
				}
				if(!use){
					mqttSendMsg.sendMsg(d.getSn(), DataConverter.warpAck(d.getSn(), 4));
					logger.info("un start service user:{}",d.getPhone());
				}
			}else{
				mqttSendMsg.sendMsg(d.getSn(), DataConverter.warpAck(d.getSn(), 3));
				logger.info("un register user:{}",d.getPhone());
			}
		}else if(type.equals("state")){//设备状态
			try {
				final OtherDataBean d = mapper.readValue(body, OtherDataBean.class);
				Device device = new Device();
				device.setSn(d.getSn());
				device.setState(d.getState());
				deviceService.updateDeviceBySN(device);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(type.equals("alarm")){
			DeviceAlarm d = mapper.readValue(body, DeviceAlarm.class);//TODO:缓存最新的每一个设备的告警并作告警抑制
			d.setState(0);
			alarmFacade.createAlarm(d);
			List l = alarmFacade.getAlarmUsersPhone(null, 0, 100);
			if(l != null){
				for(Object o : l){
					SmsHander.sendAlarnMsg(String.valueOf(o), d.getSn(), AlarmType.getAlarm(d.getType()));
				}
			}
		}else if(type.equals("113")){//0x71开篮球箱成功
			try {
				OpenLockBean d = mapper.readValue(body, OpenLockBean.class);
				BeforeOpenDevice bod = deviceService.getBeforeOpenDevice(d.getSn(), d.getBallSn());
				logger.info("BeforeOpenDevice:{}", mapper.writeValueAsString(bod));
				if(bod != null){
					BasketBall bb = deviceService.getBasketballBySn(bod.getBallSn());
					
					UserCost uc = new UserCost();
					uc.setStartTime(DateUtils.getDate());
					uc.setUserId(bod.getUserId());
					uc.setType(DeviceTypeBean.USE_BALL);
					uc.setDeviceSn(bod.getSn());
					uc.setState(0);
					uc.setBallSn(bod.getBallSn());
					uc.setDeviceSn(bod.getSn());
					uc.setAddress(bod.getAddress());
					bb.setState(1);
					tradeService.useBasketball(uc, bb);
					deviceService.deleteBeforeOpenDevice(d.getSn(), bb.getSn());
					logger.info("openball:{} succeed",d.getSn());
				}else{
					logger.info("ball-lock-state-change:{} succeed",d.getSn());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(type.equals("114")){
			try {
				OpenLockBean d = mapper.readValue(body, OpenLockBean.class);
				BeforeOpenDevice bod = deviceService.getBeforeOpenDevice(d.getSn(), d.getNum());
				logger.info("BeforeOpenDevice:{}", mapper.writeValueAsString(bod));
				StorageBox sb = deviceService.getStorageboxByDeviceSnAndNum(bod.getSn(), bod.getNum());
				sb.setState(1);
				
				UserCost uc = new UserCost();
				uc.setStartTime(DateUtils.getDate());
				uc.setUserId(bod.getUserId());
				uc.setType(DeviceTypeBean.USE_BOX);
				uc.setDeviceSn(bod.getSn());
				//uc.setBallSn(device.getBallSn());
				uc.setNum(sb.getNum());
				uc.setState(0);
				uc.setDeviceSn(bod.getSn());
				uc.setAddress(bod.getAddress());
				tradeService.useStorageBox(uc, sb);
				deviceService.deleteBeforeOpenDevice(d.getSn(), sb.getNum());
				logger.info("openbox:{}-{} succeed", d.getSn(),d.getNum());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(type.equals("cmd")){
			try {
				CmdTransBean d = mapper.readValue(body, CmdTransBean.class);
				mqttSendMsg.sendMsg(d.getSn(), d.getCmd());
				logger.info("send-cmd:{}-{}", d.getSn(), MqttOpenTool.getPayload(d.getCmd()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
