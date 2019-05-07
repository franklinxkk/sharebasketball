package com.bblanqiu.module;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bblanqiu.common.mysql.model.BeforeOpenDevice;
import com.bblanqiu.common.mysql.model.StorageBox;
import com.bblanqiu.common.mysql.model.UserCost;
import com.bblanqiu.common.util.DateUtils;
import com.bblanqiu.module.device.bean.DeviceTypeBean;
import com.bblanqiu.module.device.service.DeviceService;
import com.bblanqiu.module.mqtt.bean.OpenLockBean;
import com.bblanqiu.module.user.service.TradeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/root-context.xml", "classpath:spring/servlet-context.xml" })   
public class DataTest {
	private static Logger logger = LoggerFactory.getLogger(DataTest.class);
	@Autowired
	ObjectMapper mapper;
	@Autowired
	DeviceService deviceService;
	@Autowired
	TradeService tradeService;
	@Test
	public void test(){
		OpenLockBean olb = new OpenLockBean();
		olb.setBallSn("00000000");
		olb.setSn("00000000");
		byte[] body = null;
		try {
			body = mapper.writeValueAsBytes(olb);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			OpenLockBean d = mapper.readValue(body, OpenLockBean.class);
			logger.info("brefore openbox:{}",d.getSn());
			BeforeOpenDevice bod = deviceService.getBeforeOpenDevice(d.getSn());
			logger.info("BeforeOpenDevice:{}", mapper.writeValueAsString(bod));
			StorageBox sb = deviceService.getStorageboxByDeviceSnAndNum(bod.getSn(), bod.getNum());
			logger.info("StorageBox:{}", mapper.writeValueAsString(sb));
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
	}
}
