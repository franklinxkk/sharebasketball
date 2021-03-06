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
@Table(name="device_data")
public class DeviceData {
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
	@Column(name = "lock_state")
	private String lockState;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "upload_time", updatable = false)
	private Date uploadTime;
	
	public DeviceData() {
		super();
	}

	public DeviceData(String sn, String bssid, String ip, Double longitude,
			Double latitude, String imsi, Integer stance, Integer wet,
			Integer temp, Integer power, Integer signalStrength, Date uploadTime) {
		super();
		this.sn = sn;
		this.bssid = bssid;
		this.ip = ip;
		this.longitude = longitude;
		this.latitude = latitude;
		this.imsi = imsi;
		this.stance = stance;
		this.wet = wet;
		this.temp = temp;
		this.power = power;
		this.signalStrength = signalStrength;
		this.uploadTime = uploadTime;
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

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	@Override
	public String toString() {
		return "DeviceData [id=" + id + ", sn=" + sn + ", bssid=" + bssid
				+ ", ip=" + ip + ", longitude=" + longitude + ", latitude="
				+ latitude + ", imsi=" + imsi + ", stance=" + stance + ", wet="
				+ wet + ", temp=" + temp + ", power=" + power
				+ ", signalStrength=" + signalStrength + ", lockState="
				+ lockState + ", uploadTime=" + uploadTime + "]";
	}

}
