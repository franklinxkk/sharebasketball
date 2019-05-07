package com.bblanqiu.module.user.bean;

import javax.validation.constraints.NotNull;

public class PhoneLoginBean {
	@NotNull
	private String phone;
	@NotNull
	private String captcha;
	@NotNull
	private String clientId;
	private Integer pType = 2;
	private Double longitude;
	private Double latitude;
	
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	public Integer getpType() {
		return pType;
	}
	public void setpType(Integer pType) {
		this.pType = pType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCaptcha() {
		return captcha;
	}
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	@Override
	public String toString() {
		return "PhoneLoginBean [phone=" + phone + ", captcha=" + captcha
				+ ", clientId=" + clientId + ", pType=" + pType
				+ ", longitude=" + longitude + ", latitude=" + latitude + "]";
	}
	
}
