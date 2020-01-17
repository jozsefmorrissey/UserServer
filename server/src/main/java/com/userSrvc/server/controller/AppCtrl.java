package com.userSrvc.server.controller;

import java.nio.file.AccessDeniedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.UserSrvc;
import com.userSrvc.server.entities.UUser;
import com.userSrvc.server.service.impl.AppSrvcImpl;

@Controller
public class AppCtrl {

	@Autowired
	AopAuth<UUserAbs, App> aopAuth;
	
	@Autowired
	UserSrvc<UUserAbs> userSrvc;
	
	@Autowired
	AppSrvcImpl appSrvc;
	
    @GetMapping("/rules")
    public String getRules() {
    	try {
			aopAuth.userRequrired();
	        return "/rules/configure.html";
		} catch (AccessDeniedException e) {
			return getLogin();
		}
    }

    @CrossOrigin
    @PostMapping("/visibility")
    public String postVisibility(Model model, @RequestBody String jsonStr) {
		appSrvc.buildVisibilityModel(model, jsonStr);
        return "/visibility/visibility.html";
    }

    @CrossOrigin
    @GetMapping("/visibility")
    public String getVisibility(Model model) {
    	UUserAbs u = aopAuth.getCurrentUser();
		appSrvc.buildVisibilityModel(model, aopAuth.getCurrentUser());
        return "/visibility/visibility.html";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "/login/login.html";
    }

    @PostMapping("/login")
    public String postLogin(Model model, @ModelAttribute("user") UUser user) throws Exception {
    	userSrvc.login(user);
    	aopAuth.setCurrentApp(AopAuth.getBackendApp());
        return getVisibility(model);
    }
}
