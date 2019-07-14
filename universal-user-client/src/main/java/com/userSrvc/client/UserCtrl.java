package com.userSrvc.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;

public class UserCtrl <U extends UUserAbs>{
	@Autowired
	UserSrvcExt<U> userSrvc;

	@PostMapping("/get/{emailOid}")
	public U get(@PathVariable String emailOid) throws RestResponseException {
		return userSrvc.get(emailOid);
	}

	@PostMapping("/update")
	public void update(@RequestBody U user) throws RestResponseException {
		userSrvc.update(user);
	}

	@PostMapping("/add")
	public void add(@RequestBody U user) throws RestResponseException {
		userSrvc.add(user);
	}

	@PostMapping("/authinticate")
	public void authinticate() throws RestResponseException {
		userSrvc.authinticateUser(AopAuth.getCurrentUser());
	}

	@PostMapping("/update/password")
	public void updatePassword(@RequestBody U user) throws RestResponseException {
		userSrvc.updatePassword(user);
	}

	@PostMapping("/reset/password")
	public void resetPassword(@RequestBody UserUrl<U> userUrl) throws RestResponseException {
		userSrvc.resetPassword(userUrl);
	}


	@PostMapping("/login")
	public U login() throws RestResponseException {
		return userSrvc.loginUser(AopAuth.getCurrentUser());
	}
}
