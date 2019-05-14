package com.userSrvc.client.enums;

public enum UUSER_ENDPOINT {
	LOGIN("login");
	
	private String endpoint;
	
	private UUSER_ENDPOINT (String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String toString() {
		return endpoint;
	}
}
