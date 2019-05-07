package com.bblanqiu.module.admin.bean;

public class UserAllBean {
	private Integer id;
	private String phone;
	private String name;
	private String nickname;
	private String icon;
	private Double longitude;
	private Double latitude;
	private String question;
	private String answer;
	private String city;
	private Double money;
	private Integer credit;
	private Integer score;
	private Integer tCount;
	private Integer pType;
	
	public Integer getpType() {
		return pType;
	}
	public void setpType(Integer pType) {
		this.pType = pType;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public Integer getCredit() {
		return credit;
	}
	public void setCredit(Integer credit) {
		this.credit = credit;
	}
	public Integer gettCount() {
		return tCount;
	}
	public void settCount(Integer tCount) {
		this.tCount = tCount;
	}
	@Override
	public String toString() {
		return "UserAllBean [id=" + id + ", phone=" + phone + ", name=" + name
				+ ", nickname=" + nickname + ", icon=" + icon + ", longitude="
				+ longitude + ", latitude=" + latitude + ", question="
				+ question + ", answer=" + answer + ", city=" + city
				+ ", money=" + money + ", credit=" + credit + ", score="
				+ score + ", tCount=" + tCount + ", pType=" + pType + "]";
	}
	
}
