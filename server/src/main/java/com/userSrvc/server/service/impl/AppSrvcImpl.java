package com.userSrvc.server.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.InvalidEndpointRequestException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.services.impl.AppSrvcAbs;
import com.userSrvc.client.util.Util;
import com.userSrvc.server.entities.FieldScope;
import com.userSrvc.server.repo.AppAccessTokenRepo;

@Service
public class AppSrvcImpl extends AppSrvcAbs<App, AppAccessTokenRepo> {
	
	public void buildVisibilityModel(Model model, Object obj) {
		buildVisibilityModel(model, Util.toJson(obj));
	}
	
	public void buildVisibilityModel(Model model, String jsonStr) {
		JsonObject jsonObj = Util.toJsonObj(jsonStr);
        Map<String, String> fieldMap = new HashMap<String, String>();
        
        for (String fieldName : jsonObj.keySet()) {
           	if (Util.strExists(fieldName, privateFields)) {
           		fieldMap.put(fieldName, FieldScope.PRIVATE);
           	} else if (Util.strExists(fieldName, publicFields)) {
           		fieldMap.put(fieldName, FieldScope.PUBLIC);
           	} else {
           		fieldMap.put(fieldName, FieldScope.ASSOCIATE);
           	}
        }

        model.addAttribute("fields", fieldMap);
        model.addAttribute("USER_SCOPES", FieldScope.USER_SCOPES);
        model.addAttribute("APP_LISTS", FieldScope.APP_LISTS);
	}	
}
