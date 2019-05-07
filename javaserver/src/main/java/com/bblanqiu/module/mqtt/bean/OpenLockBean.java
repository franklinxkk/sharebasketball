package com.bblanqiu.module.mqtt.bean;

import java.util.Date;

public class OpenLockBean {
	private String sn;
	private String ballSn;
	private Integer num;
	private Integer type;
	private Date time;
	
	public String getBallSn() {
		return ballSn;
	}
	public void setBallSn(String ballSn) {
		this.ballSn = ballSn;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
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
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "OpenLockBean [sn=" + sn + ", ballSn=" + ballSn + ", num=" + num
				+ ", type=" + type + ", time=" + time + "]";
	}
	
	
}
