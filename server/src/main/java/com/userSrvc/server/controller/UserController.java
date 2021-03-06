package com.userSrvc.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.server.entities.User;
import com.userSrvc.server.entities.UserUrl;
import com.userSrvc.server.service.UserSrvc;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {
	@Autowired
	UserSrvc userSrvc;
	
	@PostMapping("/add")
	public void addUser(@RequestBody User user) throws Exception {
		userSrvc.addUser(user);
	}

	@PostMapping("/login")
	public User loginUser(@RequestBody User user) throws Exception {
		return userSrvc.loginUser(user);
	}

	@PostMapping("/get")
	public User getUser(@RequestBody User user) throws Exception {
		return userSrvc.getUser(user.getEmail());
	}
	
	@PostMapping("/authinticate")
	public User authinticateUser(@RequestBody User user) throws Exception {
		return userSrvc.authinticate(user);
	}
	
	@PostMapping("/update/password")
	public void updatePassword(@RequestBody User user) throws Exception {
		userSrvc.updatePassword(user);
	}

	@PostMapping("/reset/password")
	public void resetPassword(@RequestBody UserUrl userUrl) throws Exception {
		userSrvc.resetPassword(userUrl.getUser(), userUrl.getUrl());
	}
}
