package com.userSrvc.server.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.util.Pssst;
import com.userSrvc.server.utils.HtmlString;


@Service
public class EmailServiceImpl {
	
	static String API_KEY = Pssst.get("usvc", "mailgun-api-key");
	static String DOMAIN_NAME = "mg." + "clockit.org";
	static String ADMIN = "UUser clockit.org <passwordReset@clockit.org>";
	static String PASSWORD_RESET = "Password Reset";
	static String HTML = "text/html";
	
	public static void main(String...args) throws UnirestException {
		send(HTML, ADMIN, "TEST", new HtmlString("<h1>Hello World</h1>"), "jozsef.morrissey@gmail.com");
	}
	
	
	public static String send(String contentType, String from, String subject, HtmlString content, String...to) throws UnirestException {
		HttpResponse<String> request = Unirest.post("https://api.mailgun.net/v3/" + DOMAIN_NAME + "/messages")
			.basicAuth("api", API_KEY)
			.header("Content-Type", contentType)
			.queryString("from", from)
			.queryString("to", String.join(";", to))
			.queryString("subject", subject)
			.queryString("html", content)
			.asString();
		
		String res = request.getBody();
		System.out.println(res);
		return res;
	}
	
	public static String resetPassword(UUserAbs user, String url) throws UnirestException {
		HashMap<String, Object> scope = new HashMap<String, Object>();
		String token = user.getToken();
		String email = user.getEmail();
		scope.put("name", user.getFullname());
		url += (url.endsWith("/") ? "" : "/") + URLEncoder.encode(user.getEmail()) + "/" + URLEncoder.encode(token);
		scope.put("url", url);
		scope.put("token", token);
		HtmlString html = new HtmlString(scope, "./src/main/resources/static/emailTemplates/password-reset.html");
		return send(HTML, ADMIN, PASSWORD_RESET, html, email);
	}
	
}
