package com.emmeni.innov.exception;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class InnovException extends RuntimeException {

	private int statusCode;
	
	public InnovException() {
		this.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public InnovException(String message) {
		this(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
	}

	public InnovException(String message, Object... args) {
		this(HttpStatus.INTERNAL_SERVER_ERROR.value(), MessageFormatter.arrayFormat(message, args).getMessage());
	}
	
	public InnovException(int code, String message, Object... args) {
		this(code, MessageFormatter.arrayFormat(message, args).getMessage());
	}

	public InnovException(int statusCode, String message) {
		super(message);
		this.setStatusCode(statusCode);
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
