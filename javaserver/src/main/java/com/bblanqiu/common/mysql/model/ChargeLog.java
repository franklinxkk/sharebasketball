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
@Table(name="charge_log")
public class ChargeLog {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	@Column(name = "user_id")
	private Integer userId;
	/**
	 * 金额
	 */
	@Column(name = "charge_quota")
	private Integer chargeQuota;
	/**
	 * 1 微信 2支付宝
	 */
	private Integer type;
	/**
	 * 1充值成功 0等待入账
	 */
	private Integer state;
	private String outtradeno;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false)
	private Date createTime;
	
	public String getOuttradeno() {
		return outtradeno;
	}
	public void setOuttradeno(String outtradeno) {
		this.outtradeno = outtradeno;
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
	public Integer getChargeQuota() {
		return chargeQuota;
	}
	public void setChargeQuota(Integer chargeQuota) {
		this.chargeQuota = chargeQuota;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "ChargeLog [id=" + id + ", userId=" + userId + ", chargeQuota="
				+ chargeQuota + ", type=" + type + ", state=" + state
				+ ", outtradeno=" + outtradeno + ", createTime=" + createTime
				+ "]";
	}
	
}
