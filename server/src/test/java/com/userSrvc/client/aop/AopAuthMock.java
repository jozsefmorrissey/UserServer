package com.userSrvc.client.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.UserSrvcExt;

@Component
@Aspect
public class AopAuthMock extends AopAuth<UUserAbs> {
	
	@Autowired
	private UserSrvcExt<UUserAbs> userSrvc;
	
	private static UUserAbs currentUser;
	
	@Override
	protected UserSrvcExt<UUserAbs> getUserSrvc() {
		return userSrvc;
	}

	@Override
	public HttpServletRequest getCurrentRequest() {
		return buildMockRequest();
	}
	
	@Override
	public UUserAbs getCurrentUser() {
		return currentUser;
	}

	public static HttpServletRequest buildMockRequest() {
		MockHttpServletRequest hsr = new MockHttpServletRequest();
		hsr.addHeader(PASSWORD, currentUser.getPassword());
		hsr.addHeader(TOKEN, currentUser.getToken());
		hsr.addHeader(EMAIL, currentUser.getEmail());
		return hsr;
	}
	
	public static void reset() {
		currentUser = null;
	}

	public static void setCurrentUser(UUserAbs user) {
		currentUser = user;
	}

}
