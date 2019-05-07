package com.bblanqiu.module.pay.ali.bean;

public class AliAuthBean {
	private String zhimaAuthUrl;

	public String getZhimaAuthUrl() {
		return zhimaAuthUrl;
	}

	public void setZhimaAuthUrl(String zhimaAuthUrl) {
		this.zhimaAuthUrl = zhimaAuthUrl;
	}

	@Override
	public String toString() {
		return "AliAuthBean [zhimaAuthUrl=" + zhimaAuthUrl + "]";
	}
	
}
