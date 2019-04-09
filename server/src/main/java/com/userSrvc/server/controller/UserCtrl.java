package com.userSrvc.server.controller;

import java.nio.file.AccessDeniedException;

import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.server.entities.User;
import com.userSrvc.server.entities.request.UserUrl;
import com.userSrvc.server.service.UserSrvc;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserCtrl {
	@Autowired
	UserSrvc userSrvc;
	
	@PostMapping("/add")
	public void addUser(@RequestBody User user) throws PropertyValueException {
		userSrvc.addUser(user);
	}

	@PostMapping("/login")
	public User loginUser(@RequestBody User user) throws PropertyValueException, AccessDeniedException {
		return userSrvc.loginUser(user);
	}

	@PostMapping("/get")
	public User getUser(@RequestBody User user) {
		return userSrvc.getUser(user.getEmail());
	}
	
	@PostMapping("/update")
	public void update(@RequestBody User user) throws PropertyValueException, AccessDeniedException {
		userSrvc.update(user);
	}
	
	@PostMapping("/authinticate")
	public User authinticateUser(@RequestBody User user) throws AccessDeniedException {
		return userSrvc.authinticate(user);
	}
	
	@PostMapping("/update/password")
	public void updatePassword(@RequestBody User user) throws PropertyValueException, AccessDeniedException {
		userSrvc.updatePassword(user);
	}

	@PostMapping("/reset/password")
	public void resetPassword(@RequestBody UserUrl userUrl) {
		userSrvc.resetPassword(userUrl.getUser(), userUrl.getUrl());
	}
}
