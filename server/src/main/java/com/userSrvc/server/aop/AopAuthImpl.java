package com.userSrvc.server.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.AppSrvc;
import com.userSrvc.client.services.UserSrvc;
import com.userSrvc.server.entities.UUser;

@Component
public class AopAuthImpl extends AopAuth<UUserAbs, App> {

	@Autowired
	UserSrvc<UUserAbs> userSrvc;
	
	@Autowired
	AppSrvc<App> appSrvc;
	
	@Override
	protected UserSrvc<UUserAbs> getUserSrvc() {
		return userSrvc;
	}

	@Override
	protected AppSrvc<App> getAppSrvc() {
		return appSrvc;
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
