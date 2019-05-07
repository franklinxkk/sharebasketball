package com.bblanqiu.common.bean;

public class BasicResultBean {
	private long total;
	private int cursor;
	private int limit;
	private Object result;
	private boolean state = true;
	
	public BasicResultBean(long total, int cursor, int limit, Object result) {
		this.total = total;
		this.cursor = cursor;
		this.limit = limit;
		this.result = result;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	public int getCursor() {
		return cursor;
	}
	public void setCursor(int cursor) {
		this.cursor = cursor;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "BasicResultBean [total=" + total + ", cursor=" + cursor
				+ ", limit=" + limit + ", result=" + result + ", state="
				+ state + "]";
	}
	
}
