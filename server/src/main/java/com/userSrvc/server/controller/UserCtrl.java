package com.userSrvc.server.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.services.SrvcProps;
import com.userSrvc.client.util.AES;
import com.userSrvc.exceptions.StatisticallyImpossible;
import com.userSrvc.server.service.UserSrvc;

@CrossOrigin
@RestController
@RequestMapping
public class UserCtrl {
	@Autowired
	UserSrvc userSrvc;
	
	@Autowired
	SrvcProps srvcProps;

	@Autowired
	AES aes;
	
	@PostMapping(URI.USER_ADD)
	public UUserAbs addUser(@RequestBody UUserAbs user) throws PropertyValueException, ConstraintViolationException, StatisticallyImpossible {
		return userSrvc.addUser(user);
	}

	@PostMapping(URI.USER_LOGIN)
	public UUserAbs loginUser(@RequestBody UUserAbs user) throws PropertyValueException, AccessDeniedException {
		user = userSrvc.loginUser(user);
		return user;
	}

	@GetMapping(URI.USER_EMAIL_O_ID)
	public UUserAbs getUser(@PathVariable String emailOid) {
		if (emailOid.matches("^(-|)[0-9]*$")) {
			return userSrvc.getUser(Long.parseLong(emailOid));
		}
		UUserAbs user = userSrvc.getUser(emailOid);
		return user;
	}

	@PostMapping(URI.USER_ALL)
	public List<UUserAbs> getUser(@RequestBody List<Long> ids) {
		return userSrvc.getUsers(ids);
	}

	@PostMapping(URI.USER_UPDATE)
	public void update(@RequestBody UUserAbs user) throws PropertyValueException, AccessDeniedException {
		userSrvc.update(user);
	}
	
	@PostMapping(URI.USER_AUTH)
	public UUserAbs authinticateUser(@RequestBody UUserAbs user) throws AccessDeniedException {
		return userSrvc.authinticate(user);
	}
	
	@PostMapping(URI.USER_UPDATE_PASSWORD)
	public void updatePassword(@RequestBody UUserAbs user) throws PropertyValueException, AccessDeniedException {
		userSrvc.updatePassword(user);
	}

	@PostMapping(URI.USER_RESET_PASSWORD)
	public void resetPassword(@RequestBody UserUrl<UUserAbs> userUrl) {
		userSrvc.resetPassword(userUrl.getUser(), userUrl.getUrl());
	}
}
