package com.bblanqiu.module.alarm.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bblanqiu.common.mysql.model.AlarmOpM;
import com.bblanqiu.common.mysql.model.DeviceAlarm;
import com.bblanqiu.common.service.MysqlBasicService;
import com.bblanqiu.common.util.MysqlHqlUtil;
@Service
public class AlarmService extends MysqlBasicService{
	
	public void createAlarm(DeviceAlarm da) throws Exception{
		add(da);
	}
	public void updateAlarm(DeviceAlarm da){
		update(MysqlHqlUtil.getSingleUpdateHql(da, "id"));
	}
	public DeviceAlarm getAlarm(Integer id){
		return getById(DeviceAlarm.class.getName(), id);
	}
	public List<DeviceAlarm> getAlarms(Integer type,Integer cursor,Integer limit) throws Exception{
		String hql = "from "+DeviceAlarm.class.getName()+" where 1=1 ";
		if(type != null){
			hql+=" and type="+type;
		}
		hql += " order by alarmTime desc ";
		return gets(hql, cursor, limit);
	}
	
	public long getAlarmCount(Integer type)throws Exception{
		String hql="select count(*) from " + DeviceAlarm.class.getName() + " where 1=1 ";
		if(type != null){
			hql+=" and type="+type;
		}
		return getTotalCountByHql(hql);
	}
	
	public void createAlarmUser(AlarmOpM da) throws Exception{
		add(da);
	}
	public void updateAlarmUser(AlarmOpM da){
		update(MysqlHqlUtil.getSingleUpdateHql(da, "id"));
	}
	public AlarmOpM getAlarmUser(Integer id){
		return getById(AlarmOpM.class.getName(), id);
	}
	public List<AlarmOpM> getAlarmUsers(Integer type,Integer cursor,Integer limit) throws Exception{
		String hql = "from "+AlarmOpM.class.getName();
		if(type != null){
			hql += " where type="+type;
		}
		return gets(hql, cursor, limit);
	}
	public long getAlarmUserCount(Integer type)throws Exception{
		String hql="select count(*) from " + AlarmOpM.class.getName() + " where 1=1 ";
		if(type != null){
			hql+=" and type="+type;
		}
		return getTotalCountByHql(hql);
	}
}
