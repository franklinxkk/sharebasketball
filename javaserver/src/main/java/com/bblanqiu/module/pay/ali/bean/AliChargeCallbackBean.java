package com.bblanqiu.module.pay.ali.bean;

import javax.validation.constraints.NotNull;

public class AliChargeCallbackBean {
	@NotNull
	private String memo;
	@NotNull
	private String result;
	@NotNull
	private String resultStatus;
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getResultStatus() {
		return resultStatus;
	}
	public void setResultStatus(String resultStatus) {
		this.resultStatus = resultStatus;
	}
	@Override
	public String toString() {
		return "AliChargeCallbackBean [memo=" + memo + ", result=" + result
				+ ", resultStatus=" + resultStatus + "]";
	}
	
}
