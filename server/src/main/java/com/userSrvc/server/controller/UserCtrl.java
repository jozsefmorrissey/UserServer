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

import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.services.SrvcProps;
import com.userSrvc.client.util.AES;
import com.userSrvc.exceptions.StatisticallyImpossible;
import com.userSrvc.server.entities.UUser;
import com.userSrvc.server.entities.UserPhoto;
import com.userSrvc.server.service.UserSrvc;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserCtrl {
	@Autowired
	UserSrvc userSrvc;
	
	@Autowired
	SrvcProps srvcProps;

	@Autowired
	AES aes;
	
	@PostMapping("/add")
	public UUser addUser(@RequestBody UUser user) throws PropertyValueException, ConstraintViolationException, StatisticallyImpossible {
		return userSrvc.addUser(user);
	}

	@PostMapping("/login")
	public UUser loginUser(@RequestBody UUser user) throws PropertyValueException, AccessDeniedException {
		user = userSrvc.loginUser(user);
		return user;
	}

	@PostMapping("/get")
	public UUser getUser(@RequestBody UUser user) {
		user = userSrvc.getUser(user);
		return user;
	}

	@PostMapping("/update")
	public void update(@RequestBody UUser user) throws PropertyValueException, AccessDeniedException {
		userSrvc.update(user);
	}
	
	@PostMapping("/authinticate")
	public UUser authinticateUser(@RequestBody UUser user) throws AccessDeniedException {
		return userSrvc.authinticate(user);
	}
	
	@PostMapping("/update/password")
	public void updatePassword(@RequestBody UUser user) throws PropertyValueException, AccessDeniedException {
		userSrvc.updatePassword(user);
	}

	@PostMapping("/reset/password")
	public void resetPassword(@RequestBody UserUrl<UUser> userUrl) {
		userSrvc.resetPassword(userUrl.getUser(), userUrl.getUrl());
	}
	
	@GetMapping("/photo/{id}")
	public List<Byte[]> getPhotos(@PathVariable long id) {
		return userSrvc.photo(id);
	}
}
