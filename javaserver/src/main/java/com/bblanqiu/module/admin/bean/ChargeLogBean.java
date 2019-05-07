package com.bblanqiu.module.admin.bean;

import java.util.Date;

public class ChargeLogBean {
	private Integer id;
	private Integer userId;
	private String phone;
	private Integer chargeQuota;
	private Integer type;
	private Integer state;
	private String outtradeno;
	private Date createTime;
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
	public Integer getChargeQuota() {
		return chargeQuota;
	}
	public void setChargeQuota(Integer chargeQuota) {
		this.chargeQuota = chargeQuota;
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
	public String getOuttradeno() {
		return outtradeno;
	}
	public void setOuttradeno(String outtradeno) {
		this.outtradeno = outtradeno;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "ChargeLogBean [id=" + id + ", userId=" + userId + ", phone="
				+ phone + ", chargeQuota=" + chargeQuota + ", type=" + type
				+ ", state=" + state + ", outtradeno=" + outtradeno
				+ ", createTime=" + createTime + "]";
	}
	
}
