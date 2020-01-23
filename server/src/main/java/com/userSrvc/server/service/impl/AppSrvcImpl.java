package com.userSrvc.server.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.services.abs.AppSrvcAbs;
import com.userSrvc.client.util.Util;
import com.userSrvc.server.constant.ACCESS;
import com.userSrvc.server.repo.AppAccessTokenRepo;

@Service
public class AppSrvcImpl extends AppSrvcAbs<App, AppAccessTokenRepo> {
	
	public void buildVisibilityModel(Model model, Object obj) {
		buildVisibilityModel(model, Util.toJson(obj));
	}
	
	public void buildVisibilityModel(Model model, String jsonStr) {
		JsonObject jsonObj = Util.toJsonObj(jsonStr);
        Map<String, ACCESS> fieldMap = new HashMap<String, ACCESS>();

        // TODO: PULL THESE FROM DATABASE
        JsonArray publicFields = (JsonArray)jsonObj.get("publicFields");
        JsonArray privateFields = (JsonArray)jsonObj.get("privateFields");
        
        for (String fieldName : jsonObj.keySet()) {
           	if (Util.strExists(fieldName, privateFields)) {
           		fieldMap.put(fieldName, ACCESS.USER_SCOPE_PRIVATE);
           	} else if (Util.strExists(fieldName, publicFields)) {
           		fieldMap.put(fieldName, ACCESS.USER_SCOPE_PUBLIC);
           	} else {
           		fieldMap.put(fieldName, ACCESS.USER_SCOPE_ASSOSIATE);
           	}
        }

        model.addAttribute("fields", fieldMap);
        model.addAttribute("USER_SCOPES", ACCESS.userScopes());
        model.addAttribute("APP_LISTS", ACCESS.appLists());
        model.addAttribute("USER_EXCEPTION_LISTS", ACCESS.userExceptionLists());
	}	
}
