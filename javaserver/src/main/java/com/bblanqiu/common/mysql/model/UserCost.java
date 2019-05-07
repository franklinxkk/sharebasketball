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
@Table(name="user_cost")
public class UserCost {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	@Column(name = "user_id")
	private Integer userId;
	/**
	 * 花费
	 */
	private Float consume;
	@Column(name = "ball_sn")
	private String ballSn;
	@Column(name = "device_sn")
	private String deviceSn;
	private Integer num;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_time", updatable = false)
	private Date startTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "end_time", updatable = false)
	private Date endTime;
	private String address;
	/**
	 * 1用球 2储物
	 */
	private Integer type;
	/**
	 * 0正在进行 1已完成
	 */
	private Integer state;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Float getConsume() {
		return consume;
	}
	public void setConsume(Float consume) {
		this.consume = consume;
	}
	public String getBallSn() {
		return ballSn;
	}
	public void setBallSn(String ballSn) {
		this.ballSn = ballSn;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
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
	@Override
	public String toString() {
		return "UserCost [id=" + id + ", userId=" + userId + ", consume="
				+ consume + ", ballSn=" + ballSn + ", deviceSn=" + deviceSn
				+ ", num=" + num + ", startTime=" + startTime + ", endTime="
				+ endTime + ", type=" + type + ", state=" + state + "]";
	}
	
}
