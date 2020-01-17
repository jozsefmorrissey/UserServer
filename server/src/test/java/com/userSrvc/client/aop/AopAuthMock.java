package com.userSrvc.client.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Component;

import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.ConnectionState;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.AppSrvc;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.server.entities.UUser;

@Component
@Aspect
public class AopAuthMock extends AopAuth<UUserAbs, App> {
	
	@Autowired
	private UserSrvcExt<UUserAbs> userSrvc;
	
	private static UUserAbs currentUser;
	
	@Autowired
	AppSrvc<App> appSrvc;

	@Override
	protected AppSrvc<App> getAppSrvc() {
		return appSrvc;
	}

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
		hsr.addHeader(ConnectionState.PASSWORD, currentUser.getPassword());
		hsr.addHeader(ConnectionState.TOKEN, currentUser.getToken());
		hsr.addHeader(ConnectionState.EMAIL, currentUser.getEmail());
		return hsr;
	}
	
	public static void reset() {
		currentUser = null;
	}

	@Override
	protected UUserAbs getNewUser() {
		return new UUser();
	}

	@Override
	protected App getNewApp() {
		return new App();
	}

}
