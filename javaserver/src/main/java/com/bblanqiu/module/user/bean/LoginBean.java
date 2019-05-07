package com.bblanqiu.module.user.bean;

import javax.validation.constraints.NotNull;

public class LoginBean {
	private String phone;
	private String email;
	@NotNull
	private String password;
	private String clientId;
	private Integer pType;

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getpType() {
		return pType;
	}
	public void setpType(Integer pType) {
		this.pType = pType;
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
	@Override
	public String toString() {
		return "LoginBean [phone=" + phone + ", email=" + email + ", password="
				+ password + ", clientId=" + clientId + ", pType=" + pType
				+ "]";
	}
	
}
