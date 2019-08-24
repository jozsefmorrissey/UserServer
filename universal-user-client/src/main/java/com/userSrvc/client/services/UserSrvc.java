package com.userSrvc.client.services;

import java.util.List;

import org.springframework.http.HttpHeaders;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.marker.Service;

public interface UserSrvc <U extends UUserAbs> extends Service<U> {
	public U login() throws Exception;
	public U get(String emailOid) throws Exception;
	public U update(U user) throws Exception;
	public U updateSrvc(U user) throws Exception;
	public U authinticate() throws Exception;
	public U authinticate(U user) throws Exception;
	public void updateEmail(String newEmail) throws Exception;
	public void updatePassword() throws Exception;
	public void resetPassword(String url)throws Exception;
	public U add(U user) throws Exception;

	List<U> clean(List<U> users);
	
	public static HttpHeaders getHeaders(UUserAbs user) {
		HttpHeaders httpHeaders = new HttpHeaders();
	    if (user != null) {
		    httpHeaders.add(AopAuth.EMAIL, "" + user.getEmail());
		    httpHeaders.add(AopAuth.PASSWORD, user.getPassword());
		    httpHeaders.add(AopAuth.TOKEN, user.getToken());
//		    httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON.toString());
//		    httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
	    }
	    return httpHeaders;
	}
}
