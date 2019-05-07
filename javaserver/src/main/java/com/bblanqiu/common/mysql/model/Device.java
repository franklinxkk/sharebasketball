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
@Table(name="device")
public class Device {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
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
	@Column(name = "signal_strength")
	private Integer signalStrength;
	@Column(name = "ball_sn")
	private String ballSn;
	/**
	 * 0 离线 1在线 2维修中
	 */
	private Integer state;
	private Integer type;
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", updatable = false)
	private Date createTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_time")
	private Date updateTime;
	@Column(name = "court_id")
	private Integer courtId;
	@Column(name = "court_name")
	private String courtName;
	@Column(name = "lock_state")
	private String lockState;
	
	public String getLockState() {
		return lockState;
	}

	public void setLockState(String lockState) {
		this.lockState = lockState;
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

	@Override
	public String toString() {
		return "Device [id=" + id + ", sn=" + sn + ", bssid=" + bssid + ", ip="
				+ ip + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", imsi=" + imsi + ", stance=" + stance + ", wet=" + wet
				+ ", temp=" + temp + ", power=" + power + ", signalStrength="
				+ signalStrength + ", ballSn=" + ballSn + ", state=" + state
				+ ", type=" + type + ", description=" + description
				+ ", createTime=" + createTime + ", updateTime=" + updateTime
				+ ", courtId=" + courtId + ", courtName=" + courtName
				+ ", lockState=" + lockState + "]";
	}

}
