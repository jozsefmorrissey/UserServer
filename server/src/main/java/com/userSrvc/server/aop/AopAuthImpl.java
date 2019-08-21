package com.userSrvc.server.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.UserSrvc;

@Component
public class AopAuthImpl extends AopAuth<UUserAbs> {

	@Autowired
	UserSrvc<UUserAbs> userSrvc;
	
	@Override
	protected UserSrvc<UUserAbs> getUserSrvc() {
		return userSrvc;
	}

}
