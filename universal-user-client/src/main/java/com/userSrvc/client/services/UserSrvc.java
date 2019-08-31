package com.userSrvc.client.services;

import java.util.List;

import org.springframework.http.HttpHeaders;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.marker.Service;
import com.userSrvc.client.util.DebugGui;

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
	public HttpHeaders getHeaders(U user);
	
	List<U> clean(List<U> users);
}
