package com.userSrvc.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.SrvcProps;
import com.userSrvc.client.util.AES;
import com.userSrvc.server.service.impl.UserSrvcImpl;

@CrossOrigin
@RestController
@RequestMapping
public class UserCtrl {
	@Autowired
	UserSrvcImpl userSrvc;
	
	@Autowired
	SrvcProps srvcProps;

	@Autowired
	AES aes;

	@Autowired
	AopAuth<?> aopAuth;

	@PostMapping(URI.USER_ADD)
	public UUserAbs add(@RequestBody UUserAbs user) throws Exception {
		return userSrvc.add(user);
	}

	@GetMapping(URI.USER_LOGIN)
	public UUserAbs login() throws Exception {
		UUserAbs user = userSrvc.login();
		return user;
	}

	@GetMapping(URI.USER_EMAIL)
	public UUserAbs get(@PathVariable String email) throws NumberFormatException, Exception {
		UUserAbs user = userSrvc.get(email);
		return user;
	}

	@PostMapping(URI.USER_ALL)
	public List<UUserAbs> get(@RequestBody List<Long> ids) throws Exception {
		return userSrvc.get(ids);
	}

	@PostMapping(URI.USER_UPDATE)
	public void update(@RequestBody UUserAbs user) throws Exception {
		userSrvc.update(user);
	}
	
	@GetMapping(URI.USER_AUTH)
	public UUserAbs authinticate() throws Exception {
		return userSrvc.authinticate();
	}
	
	@GetMapping(URI.USER_UPDATE_PASSWORD)
	public void updatePassword() throws Exception {
		userSrvc.updatePassword();
	}

	@PostMapping(URI.USER_RESET_PASSWORD)
	public void resetPassword(@RequestBody String url) throws Exception {
		userSrvc.resetPassword(url);
	}
}
