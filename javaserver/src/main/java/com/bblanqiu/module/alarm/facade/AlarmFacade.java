package com.bblanqiu.module.alarm.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bblanqiu.common.cache.CacheTableNames;
import com.bblanqiu.common.cache.RedisService;
import com.bblanqiu.common.mysql.model.AlarmOpM;
import com.bblanqiu.common.mysql.model.DeviceAlarm;
import com.bblanqiu.module.alarm.service.AlarmService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AlarmFacade extends RedisService{
	@Autowired
	AlarmService alarmService;
	@Autowired
	ObjectMapper mapper;
	public List getAlarmUsersPhone(Integer type, Integer cursor, Integer limit){
		List phones = null;
		if(type == null){
			type = 1;
		}
		Object o = getValue(CacheTableNames.PREFIX_DEVICE_ALARMOPM + type);
		try {
			if(o != null){
				phones = mapper.readValue(o.toString(), List.class);
			}else{
				List<AlarmOpM> l = alarmService.getAlarmUsers(type, cursor, limit);
				if(l != null){
					phones = new ArrayList<String>();
					for(AlarmOpM ao : l){
						phones.add(ao.getPhone());
					}
					addValue(CacheTableNames.PREFIX_DEVICE_ALARMOPM + type, mapper.writeValueAsString(phones), null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return phones;
	}
	public void createAlarm(DeviceAlarm da) throws Exception{
		alarmService.createAlarm(da);
	}
}
