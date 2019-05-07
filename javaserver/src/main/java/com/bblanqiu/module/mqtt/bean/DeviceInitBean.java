package com.bblanqiu.module.mqtt.bean;

public class DeviceInitBean {
	private String deviceSn;
	private String ballSn;
	private String imsi;
	private Integer signal;
	private Integer wet;
	private Integer temp;
	private Integer power;
	private Double longitude;
	private Double latitude;
	private Integer stance;
	private Long time;
	private String lockState;
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getBallSn() {
		return ballSn;
	}
	public void setBallSn(String ballSn) {
		this.ballSn = ballSn;
	}
	public String getImsi() {
		return imsi;
	}
	public void setImsi(String imsi) {
		this.imsi = imsi;
	}
	public Integer getSignal() {
		return signal;
	}
	public void setSignal(Integer signal) {
		this.signal = signal;
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
	public Integer getStance() {
		return stance;
	}
	public void setStance(Integer stance) {
		this.stance = stance;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getLockState() {
		return lockState;
	}
	public void setLockState(String lockState) {
		this.lockState = lockState;
	}
	@Override
	public String toString() {
		return "DeviceInitBean [deviceSn=" + deviceSn + ", ballSn=" + ballSn
				+ ", imsi=" + imsi + ", signal=" + signal + ", wet=" + wet
				+ ", temp=" + temp + ", power=" + power + ", longitude="
				+ longitude + ", latitude=" + latitude + ", stance=" + stance
				+ ", time=" + time + ", lockState=" + lockState + "]";
	}

}
