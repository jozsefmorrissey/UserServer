package com.userSrvc.client.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.util.JSONMap;

import org.json.JSONArray;

@Service
public class SrvcProps {
	@Value("${uu.propery.url:https://github.com/jozsefmorrissey/UserServer/raw/master/Setup%20files/service-properties.json}")
	private String propertyUrl;
	
	private static JSONMap jsonMap;
	
	@PostConstruct
	public void setServiceProperties () throws RestResponseException, MalformedURLException, IOException, JSONException {
		String obj = IOUtils.toString(new URL(propertyUrl), Charset.forName("UTF-8"));
		jsonMap = new JSONMap(obj);
	}

	public static JSONMap getJsonMap() {
		return jsonMap;
	}
}
