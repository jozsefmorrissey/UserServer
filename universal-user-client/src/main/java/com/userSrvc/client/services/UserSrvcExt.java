package com.userSrvc.client.services;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.marker.Service;
import com.userSrvc.client.repo.UserBaseRepository;

public interface UserSrvcExt <U extends UUserAbs> extends Service<U> {
	public U loginUser(U user) throws RestResponseException;
	public U get(String emailOid) throws RestResponseException;
	public U update(U user) throws RestResponseException;
	public U updateSrvc(U user) throws RestResponseException;
	public U authinticateUser(U user) throws RestResponseException;
	public void updatePassword(U user) throws RestResponseException;
	public void resetPassword(UserUrl<U> userUrl)throws RestResponseException;
	public U add(U user) throws RestResponseException;
	public U get(long id) throws Exception;
	
	public UserBaseRepository<U> getRepo();
}
