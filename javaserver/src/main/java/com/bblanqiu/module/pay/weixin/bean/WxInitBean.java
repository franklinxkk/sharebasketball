package com.bblanqiu.module.pay.weixin.bean;

public class WxInitBean {
	private String appid;
	private String partnerid;
	private String prepayid;
	private String packagestr;
	private String noncestr;
	private String timestamp;
	private String sign;
	public WxInitBean(){};
	public WxInitBean(String appid, String partnerid, String prepayid,
			String packagestr, String noncestr, String timestamp, String sign) {
		super();
		this.appid = appid;
		this.partnerid = partnerid;
		this.prepayid = prepayid;
		this.packagestr = packagestr;
		this.noncestr = noncestr;
		this.timestamp = timestamp;
		this.sign = sign;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPartnerid() {
		return partnerid;
	}
	public void setPartnerid(String partnerid) {
		this.partnerid = partnerid;
	}
	public String getPrepayid() {
		return prepayid;
	}
	public void setPrepayid(String prepayid) {
		this.prepayid = prepayid;
	}
	public String getPackagestr() {
		return packagestr;
	}
	public void setPackagestr(String packagestr) {
		this.packagestr = packagestr;
	}
	public String getNoncestr() {
		return noncestr;
	}
	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "WxInitBean [appid=" + appid + ", partnerid=" + partnerid
				+ ", prepayid=" + prepayid + ", packagestr=" + packagestr
				+ ", noncestr=" + noncestr + ", timestamp=" + timestamp
				+ ", sign=" + sign + "]";
	}
	
	

}
