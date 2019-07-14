package com.userSrvc.client.error;

public class TryingToProtectRequiredFieldError extends Error {
	private static final long serialVersionUID = 7246689526288388068L;

	private String msg;
	public TryingToProtectRequiredFieldError(String msg) {
		this.msg = msg;
	}
	@Override
	public String getMessage() {
		return msg;
	}
}
