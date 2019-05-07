package com.bblanqiu.module.user.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.cache.CacheTableNames;
import com.bblanqiu.common.cache.RedisService;
import com.bblanqiu.common.mysql.model.BeforeOpenDevice;
import com.bblanqiu.common.util.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OrderService extends RedisService{
	@Autowired
	ObjectMapper mapper;
	public BeforeOpenDevice getOrder(String sn,Integer num){
		Object o = getValue(CacheTableNames.PREFIX_DEVICE_ORDER + sn+"-"+num);
		del(CacheTableNames.PREFIX_DEVICE_ORDER + sn+"-"+num);
		if(o == null){
			return null;
		}else{
			try {
				BeforeOpenDevice c = mapper.readValue(o.toString(), BeforeOpenDevice.class);
				return c;
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public void addOrder(Integer userId, String sn, String ballSn, Integer num,String address) throws JsonProcessingException{
		BeforeOpenDevice bod = new BeforeOpenDevice();
		bod.setUserId(userId);
		bod.setBallSn(ballSn);
		bod.setCreateTime(DateUtils.getDate());
		if(ballSn !=null){
			bod.setNum(0);
		}else{
			bod.setNum(num);
		}
		bod.setSn(sn);
		bod.setAddress(address);
		addValue(CacheTableNames.PREFIX_DEVICE_ORDER + sn+"-"+num, mapper.writeValueAsString(bod), 22L);
	}
}
