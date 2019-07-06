package com.userSrvc.client.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.util.JSONMap;

@Service
public class SrvcProps {
	@Value("${uu.propery.url:https://github.com/jozsefmorrissey/UserServer/raw/master/Setup%20files/service-properties.json}")
	private String propertyUrl;
	
	private static JSONMap jsonMap;
	private static String jsonString;
	
	@PostConstruct
	public void setServiceProperties () throws RestResponseException, MalformedURLException, IOException, JSONException {
		jsonString = IOUtils.toString(new URL(propertyUrl), Charset.forName("UTF-8"));
		jsonMap = new JSONMap(jsonString);
	}

	public static JSONMap getJsonMap() {
		return jsonMap;
	}

	public static String asString() {
		return jsonString;
	}
}
