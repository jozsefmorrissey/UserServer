package com.userSrvc.client.error;

public class DatabaseIntegrityException extends Exception {
	private static final long serialVersionUID = -4830877741234756845L;
	private String msg;

	public DatabaseIntegrityException(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String getMessage() {
		return msg;
	}
}
