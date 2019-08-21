package com.userSrvc.client.services.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.services.abs.PermissionSrvcExtAbs;

public class GenPermissionSrvcExtImpl extends PermissionSrvcExtAbs<UUserAbs> {
	private UUserAbs appUser;

	@Autowired
	UserSrvcExt<UUserAbs> userSrvc;
	
	public void setApplicationUser(UUserAbs appUser) {
		this.appUser = appUser;
	}

	@Override
	public UUserAbs getApplicationUser() {
		return appUser;
	}

	@Override
	public UserSrvcExt<UUserAbs> getUserSrvc() {
		return userSrvc;
	}
}
