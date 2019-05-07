package com.bblanqiu.module.user.captcha.bean;

import java.io.Serializable;
import java.util.Date;

public class PhoneCaptcha/* implements Serializable*/{
	private static final long serialVersionUID = -4942021467610431819L;
	private String phone;
	private String captcha;
	private Date timestamp;
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
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public String toString() {
		return "PhoneCaptcha [phone=" + phone + ", captcha=" + captcha
				+ ", timestamp=" + timestamp + "]";
	}
	
}
