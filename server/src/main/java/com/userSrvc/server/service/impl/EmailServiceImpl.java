package com.userSrvc.server.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.userSrvc.server.entities.UUser;
import com.userSrvc.server.utils.HtmlString;


@Service
public class EmailServiceImpl {
	
	static String API_KEY = "ab11bed99dbeb5d901aba1155cbbec71-898ca80e-918e6511";
	static String DOMAIN_NAME = "mg." + "clockit.org";
	static String ADMIN = "UUser clockit.org <passwordReset@clockit.org>";
	static String PASSWORD_RESET = "Password Reset";
	static String HTML = "text/html";
	
	
	public static String send(String contentType, String from, String subject, HtmlString content, String...to) throws UnirestException {
		HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
			.basicAuth("api", API_KEY)
			.header("Content-Type", contentType)
			.queryString("from", from)
			.queryString("to", String.join(";", to))
			.queryString("subject", subject)
			.queryString("html", content)
			.asString();
		
		return request.getBody();
	}
	
	public static String resetPassword(UUser user, String url) throws UnirestException {
		HashMap<String, Object> scope = new HashMap<String, Object>();
		String token = user.getUserToken();
		String email = user.getEmail();
		scope.put("name", user.getFullName());
		url += (url.endsWith("/") ? "" : "/") + URLEncoder.encode(user.getEmail()) + "/" + URLEncoder.encode(token);
		scope.put("url", url);
		scope.put("token", token);
		HtmlString html = new HtmlString(scope, "./src/main/resources/static/emailTemplates/password-reset.html");
		return send(HTML, ADMIN, PASSWORD_RESET, html, email);
	}
	
}
