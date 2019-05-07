package com.bblanqiu.common.bean;

import com.bblanqiu.common.util.PageUtil;

public class BasicPageResultBean {
	private Long total;
	private Integer cursor;
	private Integer limit;
	private long page;
	private Object result;
	private boolean state = true;

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
	public BasicPageResultBean(Long total, Integer cursor, Integer limit, Object result) {
		super();
		this.total = total;
		this.cursor = cursor;
		this.limit = limit;
		this.page = PageUtil.getTotalPage(total.intValue(), limit);
		this.result = result;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Integer getCursor() {
		return cursor;
	}

	public void setCursor(Integer cursor) {
		this.cursor = cursor;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Long getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return "BasicPageResultBean [total=" + total + ", cursor=" + cursor
				+ ", limit=" + limit + ", page=" + page + ", result=" + result
				+ "]";
	}

}
