package com.bblanqiu.module.admin.bean;

import java.util.Date;

public class DeviceBean {
	private Integer id;
	private String sn;
	private String bssid;
	private String ip;
	private Double longitude;
	private Double latitude;
	private String imsi;
	private Integer stance;
	private Integer wet;
	private Integer temp;
	private Integer power;
	private Integer signalStrength;
	private String ballSn;
	private Integer state;
	private Integer type;
	private String description;
	private Date createTime;
	private Date updateTime;
	private Integer courtId;
	private String courtName;
	private Double sumTrade;
	private String lockState;
	private Integer b;
	private Integer s1;
	private Integer s2;
	private Integer s3;
	private Integer s4;
	private Integer s5;
	private Integer s6;
	private Integer s7;
	private Integer s8;
	
	public Integer getB() {
		return b;
	}
	public void setB(Integer b) {
		this.b = b;
	}
	public Integer getS1() {
		return s1;
	}
	public void setS1(Integer s1) {
		this.s1 = s1;
	}
	public Integer getS2() {
		return s2;
	}
	public void setS2(Integer s2) {
		this.s2 = s2;
	}
	public Integer getS3() {
		return s3;
	}
	public void setS3(Integer s3) {
		this.s3 = s3;
	}
	public Integer getS4() {
		return s4;
	}
	public void setS4(Integer s4) {
		this.s4 = s4;
	}
	public Integer getS5() {
		return s5;
	}
	public void setS5(Integer s5) {
		this.s5 = s5;
	}
	public Integer getS6() {
		return s6;
	}
	public void setS6(Integer s6) {
		this.s6 = s6;
	}
	public Integer getS7() {
		return s7;
	}
	public void setS7(Integer s7) {
		this.s7 = s7;
	}
	public Integer getS8() {
		return s8;
	}
	public void setS8(Integer s8) {
		this.s8 = s8;
	}
	public String getLockState() {
		return lockState;
	}
	public void setLockState(String lockState) {
		this.lockState = lockState;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getBssid() {
		return bssid;
	}
	public void setBssid(String bssid) {
		this.bssid = bssid;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
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
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public Integer getStance() {
		return stance;
	}
	public void setStance(Integer stance) {
		this.stance = stance;
	}
	public Integer getWet() {
		return wet;
	}
	public void setWet(Integer wet) {
		this.wet = wet;
	}
	public Integer getTemp() {
		return temp;
	}
	public void setTemp(Integer temp) {
		this.temp = temp;
	}
	public Integer getPower() {
		return power;
	}
	public void setPower(Integer power) {
		this.power = power;
	}
	public Integer getSignalStrength() {
		return signalStrength;
	}
	public void setSignalStrength(Integer signalStrength) {
		this.signalStrength = signalStrength;
	}
	public String getBallSn() {
		return ballSn;
	}
	public void setBallSn(String ballSn) {
		this.ballSn = ballSn;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public Integer getCourtId() {
		return courtId;
	}
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}
	public String getCourtName() {
		return courtName;
	}
	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}
	public Double getSumTrade() {
		return sumTrade;
	}
	public void setSumTrade(Double sumTrade) {
		this.sumTrade = sumTrade;
	}
	@Override
	public String toString() {
		return "DeviceBean [id=" + id + ", sn=" + sn + ", bssid=" + bssid
				+ ", ip=" + ip + ", longitude=" + longitude + ", latitude="
				+ latitude + ", imsi=" + imsi + ", stance=" + stance + ", wet="
				+ wet + ", temp=" + temp + ", power=" + power
				+ ", signalStrength=" + signalStrength + ", ballSn=" + ballSn
				+ ", state=" + state + ", type=" + type + ", description="
				+ description + ", createTime=" + createTime + ", updateTime="
				+ updateTime + ", courtId=" + courtId + ", courtName="
				+ courtName + ", sumTrade=" + sumTrade + ", lockState="
				+ lockState + ", b=" + b + ", s1=" + s1 + ", s2=" + s2
				+ ", s3=" + s3 + ", s4=" + s4 + ", s5=" + s5 + ", s6=" + s6
				+ ", s7=" + s7 + ", s8=" + s8 + "]";
	}
	
}
