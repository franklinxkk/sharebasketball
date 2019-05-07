package com.bblanqiu.module.device.bean;

import com.bblanqiu.common.mysql.model.UserCost;

public class DeviceCurrentBean {
	
	private Integer userId;
	private UserCost ball;
	private UserCost box;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public UserCost getBall() {
		return ball;
	}
	public void setBall(UserCost ball) {
		this.ball = ball;
	}
	public UserCost getBox() {
		return box;
	}
	public void setBox(UserCost box) {
		this.box = box;
	}
	@Override
	public String toString() {
		return "DeviceCurrentBean [userId=" + userId + ", ball=" + ball
				+ ", box=" + box + "]";
	}
	
	
}
