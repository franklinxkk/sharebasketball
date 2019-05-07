package com.bblanqiu.common.mysql.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="user")
public class User {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	private String name;
	private String nickname;
	private String idcard;
	private String icon;
	private String phone;
	private String email;
	private String password;
	private String storagepass;
	private String question;
	private String answer;
	private Double longitude;
	private Double latitude;
	private String province;
	private String district;
	private String city;
	
	/**
	 * 1 ios ，2android
	 */
	@Column(name = "p_type")
	private Integer pType;
	
	/**
	 * 1 普通用户 2实名认证 3芝麻信用分通过 100 系统
	 */
	private Integer type;
	/**
	 * 1手机验证通过 2身份认证 3信用授权 4
	 */
	private Integer state;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false)
	private Date createTime;

	public User() {
		super();
	}

	public User(String name, String phone, Integer pType, Date createTime) {
		super();
		this.name = name;
		this.phone = phone;
		this.pType = pType;
		this.createTime = createTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Integer getpType() {
		return pType;
	}

	public void setpType(Integer pType) {
		this.pType = pType;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", nickname=" + nickname
				+ ", idcard=" + idcard + ", icon=" + icon + ", phone=" + phone
				+ ", email=" + email + ", password=" + password
				+ ", storagepass=" + storagepass + ", question=" + question
				+ ", answer=" + answer + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", province=" + province
				+ ", district=" + district + ", city=" + city + ", pType="
				+ pType + ", type=" + type + ", state=" + state
				+ ", createTime=" + createTime + "]";
	}

}
