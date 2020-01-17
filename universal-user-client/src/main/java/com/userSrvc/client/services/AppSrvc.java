package com.userSrvc.client.services;

import org.springframework.http.HttpHeaders;

import com.userSrvc.client.entities.App;
import com.userSrvc.client.marker.Service;

public interface AppSrvc <A extends App> extends Service<A, Long> {
	public A get(Long id) throws Exception;
	public A update(A app) throws Exception;
	public A authinticate() throws Exception;
	public A authinticate(A app) throws Exception;
	public HttpHeaders getHeaders(A app);
	public A authinticate(Long parseLong, String password) throws Exception;
}
