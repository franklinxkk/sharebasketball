package com.bblanqiu.module.user.bean;

import java.util.Date;

/**
 * 用户缓存信息
 * @author franklin.li
 *
 */
public class UserCacheBean {
private Integer id;
	private String name;
	private String nickname;
	private String idcard;
	private String logo;
	private String sex;
	private String sns;
	private String phone;
	private String password;
	private Integer pType;
	private Date createTime;
	private Boolean isLogin;
	private Date loginTime;
	private String token;
	private Integer type;
	private String icon;
	private String email;
	private String storagepass;
	private String question;
	private String answer;
	private Double longitude;
	private Double latitude;
	private Integer state;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getSns() {
		return sns;
	}
	public void setSns(String sns) {
		this.sns = sns;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Boolean getIsLogin() {
		return isLogin;
	}
	public void setIsLogin(Boolean isLogin) {
		this.isLogin = isLogin;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
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
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "UserCacheBean [id=" + id + ", name=" + name + ", nickname="
				+ nickname + ", idcard=" + idcard + ", logo=" + logo + ", sex="
				+ sex + ", sns=" + sns + ", phone=" + phone + ", password="
				+ password + ", pType=" + pType + ", createTime=" + createTime
				+ ", isLogin=" + isLogin + ", loginTime=" + loginTime
				+ ", token=" + token + ", type=" + type + ", icon=" + icon
				+ ", email=" + email + ", storagepass=" + storagepass
				+ ", question=" + question + ", answer=" + answer
				+ ", longitude=" + longitude + ", latitude=" + latitude
				+ ", state=" + state + "]";
	}
	
}
