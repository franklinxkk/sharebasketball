package com.bblanqiu.module.user.bean;

public class MybbInfoBean {
	private Integer userId;
	private String nickname;
	private String name;
	private String phone;
	private Integer type;
	private String icon;
	private Integer score;
	private Float wallet;
	private String question;
	private boolean hasDevicePwd;
	private boolean hasQuestion;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public boolean isHasQuestion() {
		return hasQuestion;
	}
	public void setHasQuestion(boolean hasQuestion) {
		this.hasQuestion = hasQuestion;
	}
	public boolean isHasDevicePwd() {
		return hasDevicePwd;
	}
	public void setHasDevicePwd(boolean hasDevicePwd) {
		this.hasDevicePwd = hasDevicePwd;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Float getWallet() {
		return wallet;
	}
	public void setWallet(Float wallet) {
		this.wallet = wallet;
	}
	@Override
	public String toString() {
		return "MybbInfoBean [userId=" + userId + ", nickname=" + nickname
				+ ", name=" + name + ", phone=" + phone + ", type=" + type
				+ ", icon=" + icon + ", score=" + score + ", wallet=" + wallet
				+ ", question=" + question + ", hasDevicePwd=" + hasDevicePwd
				+ ", hasQuestion=" + hasQuestion + "]";
	}
	
}
