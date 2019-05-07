package com.bblanqiu.module.mqtt.bean;

public class DeviceAlarmBean {
	private String sn;
	private int type;
	private String value;
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "DeviceAlarmBean [sn=" + sn + ", type=" + type + ", value="
				+ value + "]";
	}
	
}
