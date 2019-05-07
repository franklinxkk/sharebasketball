package com.bblanqiu.module.user.bean;

public class PasswordBean {
	private String phone;
	private String email;
	private String password;
	private String oldPassword;
	private String newPassword;
	private String captcha;
	private String clientId;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
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
		return "PasswordBean [phone=" + phone + ", email=" + email
				+ ", password=" + password + ", oldPassword=" + oldPassword
				+ ", newPassword=" + newPassword + ", captcha=" + captcha
				+ ", clientId=" + clientId + "]";
	}
	
	
}
