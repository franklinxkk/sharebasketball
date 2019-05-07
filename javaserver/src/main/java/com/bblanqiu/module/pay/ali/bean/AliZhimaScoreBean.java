package com.bblanqiu.module.pay.ali.bean;

public class AliZhimaScoreBean {
	private String bizNo;
	private String openid;
	private String score;
	
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getBizNo() {
		return bizNo;
	}
	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	@Override
	public String toString() {
		return "AliZhimaScoreBean [bizNo=" + bizNo + ", openid=" + openid
				+ ", score=" + score + "]";
	}
	
}
