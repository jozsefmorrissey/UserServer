package com.userSrvc.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.server.entities.request.UserVersionUser;
import com.userSrvc.server.service.UserVersionSrvc;

@CrossOrigin
@RestController
@RequestMapping("version")
public class UserVersionCtrl {
	@Autowired
	UserVersionSrvc uvs;

	@PostMapping("/update")
	public void update(@RequestBody UserVersionUser uvu) throws Exception {
		System.out.println("PRINTOUT:::::::::::::" + uvu);
		uvs.update(uvu.getUserVersion(), uvu.getUser());
	}
	
	@GetMapping("/{email}/{identifier}")
	public String get(@PathVariable String email,@PathVariable String identifier) throws Exception {
		System.out.println(email + ":::::::" + identifier);
		return uvs.get(email, identifier);
	}
}
