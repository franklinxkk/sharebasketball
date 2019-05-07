package com.bblanqiu.module.user.bean;

public class UserCreateBean {
	private String phone;
	
	private String email;
	
	private String password;
	
	private Integer pType = 1;
	
	private String clientId;
	
	private String captcha;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getpType() {
		return pType;
	}
	public void setpType(Integer pType) {
		this.pType = pType;
	}
	@Override
	public String toString() {
		return "UserCreateBean [phone=" + phone + ", email=" + email
				+ ", password=" + password + ", pType=" + pType + ", clientId="
				+ clientId + ", captcha=" + captcha
				+ "]";
	}
	
	
}
