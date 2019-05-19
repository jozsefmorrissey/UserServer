package com.userSrvc.client.services;

import java.util.List;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;

public interface UserSrvcExt {
	public <U extends UUserAbs> U loginUser(U user) throws RestResponseException;
	public <U extends UUserAbs> U getUser(U user) throws RestResponseException;
	public <U extends UUserAbs> List<U> getUsers(List<Long> ids) throws RestResponseException;
	public <U extends UUserAbs> U update(U user) throws RestResponseException;
	public <U extends UUserAbs> U authinticateUser(U user) throws RestResponseException;
	public <U extends UUserAbs> void updatePassword(U user) throws RestResponseException;
	public <U extends UUserAbs> void resetPassword(UserUrl<U> userUrl)throws RestResponseException;
	public <U extends UUserAbs> U add(U user) throws RestResponseException;
}
