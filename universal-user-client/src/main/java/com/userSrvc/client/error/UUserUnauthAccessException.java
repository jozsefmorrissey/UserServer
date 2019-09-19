package com.userSrvc.client.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
public class UUserUnauthAccessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4830877741234756845L;
	private String msg;
	public UUserUnauthAccessException(String msg) {
		this.msg = msg;
	}
	@Override
	public String getMessage() {
		return msg;
	}
}
