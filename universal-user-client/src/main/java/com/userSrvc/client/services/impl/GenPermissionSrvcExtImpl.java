package com.userSrvc.client.services.impl;

import org.springframework.stereotype.Service;

import com.userSrvc.client.entities.UUserAbs;

@Service
public class GenPermissionSrvcExtImpl extends PermissionSrvcExtImpl<UUserAbs> {
	private UUserAbs appUser;
	
	public void setApplicationUser(UUserAbs appUser) {
		this.appUser = appUser;
	}

	@Override
	public UUserAbs getApplicationUser() {
		return appUser;
	}
}
