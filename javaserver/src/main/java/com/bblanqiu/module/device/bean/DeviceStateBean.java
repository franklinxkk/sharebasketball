package com.bblanqiu.module.device.bean;

public class DeviceStateBean {
	private int state;
	private boolean isBasketballUsed;
	private int freeStorageBox;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public DeviceStateBean() {
	}
	public DeviceStateBean(boolean isBasketballUsed, int freeStorageBox) {
		super();
		this.isBasketballUsed = isBasketballUsed;
		this.freeStorageBox = freeStorageBox;
	}
	public boolean isBasketballUsed() {
		return isBasketballUsed;
	}
	public void setBasketballUsed(boolean isBasketballUsed) {
		this.isBasketballUsed = isBasketballUsed;
	}
	public int getFreeStorageBox() {
		return freeStorageBox;
	}
	public void setFreeStorageBox(int freeStorageBox) {
		this.freeStorageBox = freeStorageBox;
	}
	@Override
	public String toString() {
		return "DeviceStateBean [state=" + state + ", isBasketballUsed="
				+ isBasketballUsed + ", freeStorageBox=" + freeStorageBox + "]";
	}
	
}
