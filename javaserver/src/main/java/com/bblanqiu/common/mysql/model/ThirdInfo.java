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
@Table(name="third_info")
public class ThirdInfo {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private Integer id;
	@Column(name = "user_id")
	private Integer userId;
	@Column(name = "zhima_auth_transaction_id")
	private String zhimaAuthTransactionId;
	@Column(name = "zhima_auth_biz_no")
	private String zhimaAuthBizNo;
	@Column(name = "zhima_score_transaction_id")
	private String zhimaScoreTransactionId;
	@Column(name = "zhima_score_biz_no")
	private String zhimaScoreBizNo;
	@Column(name = "zhima_auth_url")
	private String zhimaAuthUrl;
	@Column(name = "zhima_auth_passed")
	private Integer zhimaAuthPassed;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "zhima_auth_time", updatable = false)
	private Date zhimaAuthTime;
	private String name;
	private String no;
	private String zmopenid;
	private String zmscore;
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
	public String getZhimaAuthTransactionId() {
		return zhimaAuthTransactionId;
	}
	public void setZhimaAuthTransactionId(String zhimaAuthTransactionId) {
		this.zhimaAuthTransactionId = zhimaAuthTransactionId;
	}
	public String getZhimaAuthBizNo() {
		return zhimaAuthBizNo;
	}
	public void setZhimaAuthBizNo(String zhimaAuthBizNo) {
		this.zhimaAuthBizNo = zhimaAuthBizNo;
	}
	public String getZhimaScoreTransactionId() {
		return zhimaScoreTransactionId;
	}
	public void setZhimaScoreTransactionId(String zhimaScoreTransactionId) {
		this.zhimaScoreTransactionId = zhimaScoreTransactionId;
	}
	public String getZhimaScoreBizNo() {
		return zhimaScoreBizNo;
	}
	public void setZhimaScoreBizNo(String zhimaScoreBizNo) {
		this.zhimaScoreBizNo = zhimaScoreBizNo;
	}
	public String getZhimaAuthUrl() {
		return zhimaAuthUrl;
	}
	public void setZhimaAuthUrl(String zhimaAuthUrl) {
		this.zhimaAuthUrl = zhimaAuthUrl;
	}
	public Integer getZhimaAuthPassed() {
		return zhimaAuthPassed;
	}
	public void setZhimaAuthPassed(Integer zhimaAuthPassed) {
		this.zhimaAuthPassed = zhimaAuthPassed;
	}
	public Date getZhimaAuthTime() {
		return zhimaAuthTime;
	}
	public void setZhimaAuthTime(Date zhimaAuthTime) {
		this.zhimaAuthTime = zhimaAuthTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getZmopenid() {
		return zmopenid;
	}
	public void setZmopenid(String zmopenid) {
		this.zmopenid = zmopenid;
	}
	public String getZmscore() {
		return zmscore;
	}
	public void setZmscore(String zmscore) {
		this.zmscore = zmscore;
	}
	@Override
	public String toString() {
		return "ThirdInfo [id=" + id + ", userId=" + userId
				+ ", zhimaAuthTransactionId=" + zhimaAuthTransactionId
				+ ", zhimaAuthBizNo=" + zhimaAuthBizNo
				+ ", zhimaScoreTransactionId=" + zhimaScoreTransactionId
				+ ", zhimaScoreBizNo=" + zhimaScoreBizNo + ", zhimaAuthUrl="
				+ zhimaAuthUrl + ", zhimaAuthPassed=" + zhimaAuthPassed
				+ ", zhimaAuthTime=" + zhimaAuthTime + ", name=" + name
				+ ", no=" + no + ", zmopenid=" + zmopenid + ", zmscore="
				+ zmscore + "]";
	}
	
}
