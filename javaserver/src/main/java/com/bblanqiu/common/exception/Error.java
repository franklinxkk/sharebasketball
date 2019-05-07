package com.bblanqiu.common.exception;

import com.bblanqiu.common.exception.ErrorCode;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Error {
    private String request;
    @JsonProperty("error_code")
    private int errorCode;
    private String error;
    private Object[] params = null;
    private boolean state = false;
    public Error() {

    }

    public Error(String request, int errorCode, String error, Object... params) {
        this.request = request;
        this.errorCode = errorCode;
        this.error = String.format(error, params);
        /*if(params != null && params.length > 0){
        	this.params = params;
        }*/
    }

    public Error(String request, ErrorCode code, Object... params) {
        this.request = request;
        this.errorCode = code.getErrorCode();
        this.error = String.format(code.getError(), params);
        /*if(params != null && params.length > 0){
        	this.params = params;
        }*/
    }
    
    public Error(ErrorCode code, Object... params) {
        this.request = "";
        this.errorCode = code.getErrorCode();
        this.error = String.format(code.getError(), params);
        /*if(params != null && params.length > 0){
        	this.params = params;
        }*/
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Error [");
        if (request != null) {
            builder.append("request=");
            builder.append(request);
            builder.append(", ");
        }
        builder.append("errorCode=");
        builder.append(errorCode);
        builder.append(", ");
        if (error != null) {
            builder.append("error=");
            builder.append(error);
        }
        builder.append("]");
        return builder.toString();
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}
    
}
