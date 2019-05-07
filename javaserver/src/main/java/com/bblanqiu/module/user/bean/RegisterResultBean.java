package com.bblanqiu.module.user.bean;


public class RegisterResultBean {
	private String token;
	private Integer userId;
	private String phone;
	private Integer type;
	
	public RegisterResultBean(String token, Integer userId, String phone,Integer type) {
		super();
		this.token = token;
		this.userId = userId;
		this.phone = phone;
		this.type = type;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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

	@Override
	public String toString() {
		return "RegisterResultBean [token=" + token + ", userId=" + userId
				+ ", phone=" + phone + ", type=" + type + "]";
	}

}
