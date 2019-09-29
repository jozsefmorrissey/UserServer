package com.userSrvc.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.UserSrvcExt;

@CrossOrigin(origins = "http://192.168.254.10:7002")
public class UserCtrl <U extends UUserAbs>{
	@Autowired
	UserSrvcExt<U> userSrvc;

	@GetMapping("/get/{emailOid}")
	public U get(@PathVariable String emailOid) throws Exception {
		return userSrvc.get(emailOid);
	}

	@PostMapping("/update")
	public void update(@RequestBody U user) throws Exception {
		userSrvc.update(user);
	}

	@PostMapping("/add")
	public void add(@RequestBody U user) throws Exception {
		userSrvc.add(user);
	}

	@PostMapping("/authinticate")
	public U authinticate() throws Exception {
		return userSrvc.authinticate();
	}

	@GetMapping("/update/password")
	public void updatePassword() throws Exception {
		userSrvc.updatePassword();
	}

	@PostMapping("/reset/password")
	public void resetPassword(@RequestBody String url) throws Exception {
		userSrvc.resetPassword(url);
	}


	@GetMapping("/login")
	public U login() throws Exception {
		return userSrvc.login();
	}
}
