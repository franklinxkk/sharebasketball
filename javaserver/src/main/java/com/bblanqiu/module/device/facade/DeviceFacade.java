package com.bblanqiu.module.device.facade;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bblanqiu.common.cache.CacheTableNames;
import com.bblanqiu.common.cache.RedisService;
import com.bblanqiu.common.mysql.model.BeforeOpenDevice;
import com.bblanqiu.common.mysql.model.StorageBox;
import com.bblanqiu.module.device.service.DeviceService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DeviceFacade extends RedisService{
	@Autowired
	DeviceService deviceService;
	@Autowired
	ObjectMapper mapper;
	/*public Object getDeviceReserves(String sn){
		return getValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+":*");
	}
	public void addDeviceReserves(String sn, Integer num){
		addValue(CacheTableNames.PREFIX_DEVICE_RESERVE + sn+":"+num, num, 15L);
	}*/
	public List<Integer> getUnReserve(String sn) throws Exception{
		List<Integer> reserveList = new ArrayList<>();
		for(int i=0;i<=8; i++){
			if(getValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+":"+i) != null){
				reserveList.add(i);
			}
		}
		return reserveList;
	}
	
	public void addReserve(String sn,Integer num){
		addValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+":"+num, String.valueOf(num), 15L);
	}
	
	public Integer getFreeStorageBox(String sn) throws Exception{
		List<StorageBox> list = deviceService.getFreeStorageboxs(sn);
		List<Integer> reserveList = getUnReserve(sn);
		for(StorageBox sb : list){
			if(!reserveList.contains(sb.getNum())){
				addReserve(sn, sb.getNum());
				return sb.getNum();
			}
		}
		return null;
	}
	public StorageBox getFreeStorageBoxInCache(String sn) throws Exception{
		List<StorageBox> sbs = new ArrayList<>();
		for(int i=1;i<=8;i++){
			Object o = getValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+":"+i);
			if(o != null){
				sbs.add((StorageBox)o);
			}
		}
		if(sbs.size() <=0){
			List<StorageBox> list = deviceService.getFreeStorageboxs(sn);
			if(list.size() > 0){
				for(StorageBox sb: list){
					addValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+":"+sb.getNum(), sb, null);
					sbs.add(sb);
				}
			}
		}
		return sbs.get(0);
	}
	
	public Object getSnUsedObject(String sn){
		return getValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+"list");
	}
	
	public void addSnUsedObject(String sn){
		addValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+"list", sn, 15L);
	}
	
	public void addSb(BeforeOpenDevice bod) throws JsonProcessingException{
		addValue(CacheTableNames.PREFIX_DEVICE_RESERVE+bod.getSn()+":"+bod.getNum(), mapper.writeValueAsString(bod), 40L);
	}
	
	public BeforeOpenDevice getSb(String sn, Integer num){
		Object o = getValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+":"+num);
		if(o != null){
			return (BeforeOpenDevice)o;
		}else{
			return null;
		}
	}
	public void addBall(BeforeOpenDevice bod) throws JsonProcessingException{
		addValue(CacheTableNames.PREFIX_DEVICE_RESERVE+bod.getSn()+":"+bod.getBallSn(), mapper.writeValueAsString(bod), 40L);
	}
	
	public BeforeOpenDevice getBall(String sn, String ballSn){
		Object o = getValue(CacheTableNames.PREFIX_DEVICE_RESERVE+sn+":"+ballSn);
		if(o != null){
			return (BeforeOpenDevice)o;
		}else{
			return null;
		}
	}
}
