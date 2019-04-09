package com.userSrvc.client.services;

import com.userSrvc.client.entities.UUser;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;

public interface UserSrvcExt {
	public <U extends UUser> U loginUser(U user) throws RestResponseException;
	public <U extends UUser> U getUser(U user) throws RestResponseException;
	public <U extends UUser> U update(U user) throws RestResponseException;
	public <U extends UUser> U authinticateUser(U user) throws RestResponseException;
	public <U extends UUser> void updatePassword(U user) throws RestResponseException;
	public <U extends UUser> void resetPassword(UserUrl userUrl)throws RestResponseException;
	public <U extends UUser> U add(U user) throws RestResponseException;
}
