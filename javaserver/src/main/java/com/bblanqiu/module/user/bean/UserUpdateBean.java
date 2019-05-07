package com.bblanqiu.module.user.bean;

public class UserUpdateBean {
	private String storagepass;
	private String question;
	private String answer;
	private String nickname;
	private String icon;
	private Integer language;
	public String getStoragepass() {
		return storagepass;
	}
	public void setStoragepass(String storagepass) {
		this.storagepass = storagepass;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getLanguage() {
		return language;
	}
	public void setLanguage(Integer language) {
		this.language = language;
	}
	@Override
	public String toString() {
		return "UserUpdateBean [storagepass=" + storagepass + ", question="
				+ question + ", answer=" + answer + ", nickname=" + nickname
				+ ", icon=" + icon + ", language=" + language + "]";
	}
	
	
}
