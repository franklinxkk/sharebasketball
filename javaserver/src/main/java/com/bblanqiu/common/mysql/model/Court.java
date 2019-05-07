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

import com.bblanqiu.common.util.DateUtils;

@Entity
@Table(name="court")
public class Court {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	/**
	 * 球场名称
	 */
	private String name;
	/**
	 * 经营周期
	 */
	private Integer operate;
	/**
	 * 营业开始时间
	 */
	@Temporal(TemporalType.TIME)
	@Column(name = "start_time", updatable = false)
	private Date startTime;
	/**
	 * 营业结束时间
	 */
	@Temporal(TemporalType.TIME)
	@Column(name = "end_time", updatable = false)
	private Date endTime;
	/**
	 * 
	 * 球场类型 1露天 2室内
	 */
	private Integer type;
	private String picture;
	private Double longitude;
	private Double latitude;
	private String province;
	private String city;
	private String district;
	private String detail;
	private String address;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false)
	private Date createTime;
	
	public Court(){};
	public Court(String name, Integer operate, Date startTime, Date endTime,
			Integer type, Double longitude, Double latitude, String province,
			String city, String district, String detail) {
		super();
		this.name = name;
		this.operate = operate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.type = type;
		this.longitude = longitude;
		this.latitude = latitude;
		this.province = province;
		this.city = city;
		this.district = district;
		this.detail = detail;
		this.createTime = DateUtils.getDate();
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
	public Integer getOperate() {
		return operate;
	}
	public void setOperate(Integer operate) {
		this.operate = operate;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
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
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "Court [id=" + id + ", name=" + name + ", operate=" + operate
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", type=" + type + ", picture=" + picture + ", longitude="
				+ longitude + ", latitude=" + latitude + ", province="
				+ province + ", city=" + city + ", district=" + district
				+ ", detail=" + detail + ", address=" + address
				+ ", createTime=" + createTime + "]";
	}
	
}
