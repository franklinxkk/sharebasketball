package com.bblanqiu.common.mysql.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="device_alarm")
public class DeviceAlarm {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	private String alarm;
	private String sn;
	/**
	 * 1姿态告警 2位置告警 3低电量高级 4篮球箱状态异常 5储物箱状态异常6温湿度异常7篮球与设备不匹配告警
	 */
	private Integer type;
	private Integer state;
	private String description;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "alarm_time", updatable = false)
	private Date alarmTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAlarm() {
		return alarm;
	}
	public void setAlarm(String alarm) {
		this.alarm = alarm;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}
	@Override
	public String toString() {
		return "DeviceAlarm [id=" + id + ", alarm=" + alarm + ", sn=" + sn
				+ ", type=" + type + ", state=" + state + ", description="
				+ description + ", alarmTime=" + alarmTime + "]";
	}
	
}
