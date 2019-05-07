package com.bblanqiu.common.exception;


public class ErrorCodeException extends RuntimeException {
	private static final long serialVersionUID = -3759054468835576764L;

	private Error error;

	public ErrorCodeException(ErrorCode code, Object... params) {
		super("bblq error:" + code.getError());
		this.error = new Error(code, params);
	}

	public ErrorCodeException(Error error) {
		super("bblq error:" + error.getError());
		this.error = error;
	}

	public Error getError() {
		return error;
	}

	public void setError(Error error) {
		this.error = error;
	}

}
