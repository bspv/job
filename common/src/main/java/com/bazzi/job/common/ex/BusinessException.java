package com.bazzi.job.common.ex;

import com.bazzi.job.common.generic.StatusCode;

public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = -2473818898937244999L;
	private String code;
	private String message;

	public BusinessException(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public BusinessException(StatusCode statusCode) {
		this.code = statusCode.getCode();
		this.message = statusCode.getMessage();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
