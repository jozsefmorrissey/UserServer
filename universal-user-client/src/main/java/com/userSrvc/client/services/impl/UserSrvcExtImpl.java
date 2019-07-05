package com.userSrvc.client.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

@Service
public class UserSrvcExtImpl<U extends UUserAbs> implements UserSrvcExt<U> {

	private Class<U> clazz = (Class<U>) UUserAbs.class;

	public U loginUser(U user) throws RestResponseException {
		return Util.restPostCall(Util.getUri(URI.USER_LOGIN), user, clazz);
	}

	@SuppressWarnings("unchecked")
	public U get(String emailOid) throws RestResponseException {
		UUserAbs uu = Util.restGetCall(Util.getUri("/user/" + emailOid), clazz);
		return (U) uu;
	}

	public U update(U user) throws RestResponseException {
		return Util.restPostCall(Util.getUri(URI.USER_UPDATE), user, clazz);
	}

	public U add(U user) throws RestResponseException {
		return Util.restPostCall(Util.getUri(URI.USER_ADD), user, clazz);
	}

	public U authinticateUser(U user) throws RestResponseException {
		return Util.restPostCall(Util.getUri(URI.USER_AUTH), user, clazz);
	}

	public void updatePassword(U user) throws RestResponseException {
		Util.restPostCall(Util.getUri(URI.USER_UPDATE_PASSWORD), user, String.class);
	}

	public void resetPassword(UserUrl<U> userUrl) throws RestResponseException {
		Util.restPostCall(Util.getUri(URI.USER_RESET_PASSWORD), userUrl, String.class);
	}


	public List<U> get(Collection<Long> ids) throws RestResponseException {
		return Util.restPostCall(Util.getUri(URI.USER_ALL), ids, ArrayList.class);
	}

	public U get(long id) throws Exception {
		return get(new Long(id).toString());
	}
}
