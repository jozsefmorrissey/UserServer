package com.userSrvc.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.server.entities.request.PageUser;
import com.userSrvc.server.entities.request.UserTopic;
import com.userSrvc.server.service.PageSrvc;

@CrossOrigin
@RestController
@RequestMapping("page")
public class PageCtrl {
	@Autowired
	PageSrvc pageSrvc;
	
	@PostMapping
	public void update(@RequestBody PageUser pageUser) throws Exception {
		pageSrvc.update(pageUser.getPage(), pageUser.getUser());
	}
	
	@GetMapping("/{identifier}")
	public String get(@PathVariable String identifier) {
		return pageSrvc.get(identifier);
	}
	
	@PostMapping("/authorization")
	public Boolean isAutorized(@RequestBody UserTopic userTopic) throws Exception {
		return pageSrvc.isAuthorized(userTopic.getUser(), userTopic.getTopic());
	}
}
