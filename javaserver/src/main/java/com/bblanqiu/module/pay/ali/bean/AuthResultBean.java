package com.bblanqiu.module.pay.ali.bean;

public class AuthResultBean {
	private boolean passed = false;
	private String reason;
	public boolean isPassed() {
		return passed;
	}
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	@Override
	public String toString() {
		return "AuthResultBean [passed=" + passed + ", reason=" + reason + "]";
	}
	
}
