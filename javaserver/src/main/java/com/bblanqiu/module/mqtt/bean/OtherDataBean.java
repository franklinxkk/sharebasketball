package com.bblanqiu.module.mqtt.bean;

public class OtherDataBean {
	private String sn;
	private String ballSn;
	private String phone;
	private Long time;
	private Integer num;
	private String pass;
	private Integer state;
	
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
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "OtherDataBean [sn=" + sn + ", ballSn=" + ballSn + ", phone="
				+ phone + ", time=" + time + ", num=" + num + ", pass=" + pass
				+ ", state=" + state + "]";
	}
	
}
