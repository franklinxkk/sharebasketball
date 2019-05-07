package com.bblanqiu.module.admin.bean;

import java.util.Date;

public class UserCostInfoBean {
	private Integer id;
	private Integer userId;
	private String phone;
	private Float consume;
	private String ballSn;
	private String deviceSn;
	private Integer num;
	private Date startTime;
	private Date endTime;
	private String address;
	private Integer type;
	private Integer state;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Float getConsume() {
		return consume;
	}
	public void setConsume(Float consume) {
		this.consume = consume;
	}
	public String getBallSn() {
		return ballSn;
	}
	public void setBallSn(String ballSn) {
		this.ballSn = ballSn;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	@Override
	public String toString() {
		return "UserCostInfoBean [id=" + id + ", userId=" + userId + ", phone="
				+ phone + ", consume=" + consume + ", ballSn=" + ballSn
				+ ", deviceSn=" + deviceSn + ", num=" + num + ", startTime="
				+ startTime + ", endTime=" + endTime + ", address=" + address
				+ ", type=" + type + ", state=" + state + "]";
	}
	
}
