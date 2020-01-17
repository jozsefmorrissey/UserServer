package com.userSrvc.client.entities;

import java.util.Arrays;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.util.Base64Utils;

import com.userSrvc.client.util.Util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionState {
	public static final String TOKEN = "UUser-Token";
	public static final String PASSWORD = "UUser-Password";
	public static final String EMAIL = "UUser-Email";
	public static final String DEVICE_IDENTIFIER = "UUser-Device-Identifier";
	public static final String APP_TOKEN = "APP-Token";
	public static final String APP_ACCESS_KEY = "App-Access-Key";
	public static final String APP_ID = "App-Id";
	
	private static String s = "~";
	private static String UUserCookie = "UUserSrvc";
	
	private String deviceId;

	private long appId;
	private String accessKey;
	private String appToken;
	
	private String userToken;
	private long userId;
	private String userPassword;
	
	private final String AUTHORIZATION_HEADER = "Authorization";
	
	public ConnectionState() {}
	
	public ConnectionState(UUserAbs user, App app) {
		if (user != null && user.getToken() != null) {
			userToken = user.getToken().getToken();
			userId = user.getId();
		}
		if (app != null && app.getToken() != null) {
			appId = app.getId();
			appToken = app.getToken().getToken();
		}
	}
	
	public ConnectionState(HttpServletRequest request) {
		Cookie cookie = Util.getCookie(UUserCookie, request);
		if (cookie != null) {
			setValuesFromAuthString(cookie.getValue());
		}
		String authHeader = request.getHeader(AUTHORIZATION_HEADER);
		if (authHeader != null) {
			setValuesFromAuthString(authHeader);
		}
	}
	
	public static void main(String...args) {
		String deviceId = "XySaAZHcigPs0RsKK7T#C3!#K$d(4L|S";
		long appId = -1;
		String appToken = "password";
		String userToken = "1JjZtmVw9";
		Long userId = 1l;
		
		String encStr = authString(deviceId, appId, appToken, userToken, userId);
		System.out.println(encStr);
		byte[] decStr = Base64Utils.decodeFromString(encStr);
		String str = new String(decStr);
		String[] pieces = str.split(s);
		System.out.println(Arrays.toString(pieces));
	}

	public String authString() {
		return authString(deviceId, appId, appToken, userToken, userId);
	}
	
	public static String authString(String deviceId, long appId, String appToken, String userToken,
			Long userId) {
		return Base64Utils.encodeToString((deviceId + s +
				appId + s +
				appToken + s +
				userToken + s +
				userId).getBytes());
	}
	
	public void setValuesFromAuthString(String str) {
		if (str == null) return;
		String[] peices = new String(Base64Utils.decodeFromString(
				str)).split(s);
		this.deviceId = nullify(peices[0]);
		this.appId = Long.parseLong(peices[1]);
		this.appToken = nullify(peices[2]);
		this.userToken = nullify(peices[3]);
		this.userId = Long.parseLong(peices[4]);
	}
	
	public String toString() {
		return Base64Utils.encodeToString((deviceId + s +
				appId + s +
				accessKey + s +
				appToken + s +
				userToken + s +
				userId).getBytes());
	}
	
	public HttpHeaders addAuthorization(HttpHeaders httpHeaders) {
		if (httpHeaders == null) {
			httpHeaders = new HttpHeaders();
		}
		httpHeaders.add(AUTHORIZATION_HEADER, this.authString());
		return httpHeaders;
	}
	
	public void addAuthorization(HttpServletResponse response) {
		String authStr = this.authString();
		response.addHeader(AUTHORIZATION_HEADER, authStr);
		Cookie cookie = new Cookie(UUserCookie, authStr);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setMaxAge(60 * 10);
		response.addCookie(cookie);
	}
	
	private String nullify(String str) {
		if (str == null || str.equals("null") || str.equals("undefined")) {
			return null;
		}
		return str;
	}
}
