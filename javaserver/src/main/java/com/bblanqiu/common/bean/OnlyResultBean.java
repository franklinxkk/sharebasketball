package com.bblanqiu.common.bean;

public class OnlyResultBean {
	private Object result;
	private boolean state = true;
	
    public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public OnlyResultBean(Object result) {
        this.result = result;
    }
    
    public OnlyResultBean(Object result, Boolean state) {
        this.result = result;
        this.state = state;
    }

    public OnlyResultBean() {
    }
}
