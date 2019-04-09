package com.userSrvc.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.client.entities.UUser;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserCtrl <U extends UUser>{
	@Autowired
	UserSrvcExt userSrvc;
	
	@PostMapping("/add")
	public void addU(@RequestBody U user) throws RestResponseException {
		userSrvc.add(user);
	}

	@PostMapping("/login")
	public U loginU(@RequestBody U user) throws RestResponseException {
		return userSrvc.loginUser(user);
	}

	@PostMapping("/get")
	public U getU(@RequestBody U user) throws RestResponseException {
		return userSrvc.getUser(user);
	}
	
	@PostMapping("/update")
	public void update(@RequestBody U user) throws RestResponseException {
		userSrvc.update(user);
	}
	
	@PostMapping("/authinticate")
	public U authinticateU(@RequestBody U user) throws RestResponseException {
		return userSrvc.authinticateUser(user);
	}
	
	@PostMapping("/update/password")
	public void updatePassword(@RequestBody U user) throws RestResponseException {
		userSrvc.updatePassword(user);
	}

	@PostMapping("/reset/password")
	public void resetPassword(@RequestBody UserUrl userUrl) throws RestResponseException {
		userSrvc.resetPassword(userUrl);
	}
}
