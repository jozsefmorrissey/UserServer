package com.userSrvc.client.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.userSrvc.client.entities.DefaultUUser;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

public class UserSrvcExtImpl<U extends UUserAbs> implements UserSrvcExt<U> {

	private DefaultUUser UUser = new DefaultUUser();
	
	private Class<U> clazz = (Class<U>) UUserAbs.class;
	
	private <R> R restGetCall(String uri, Class<R> returnedClass) throws RestResponseException {
		RestTemplate rt= new RestTemplate();
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

	    try {
	    	return rt.getForObject(uri, returnedClass);
	    } catch (HttpStatusCodeException e) {
	    	throw new RestResponseException(e.getResponseBodyAsString());
	    }
	}
	
	private <R> R restPostCall(String uri, Object entity, Class<R> returnedClass) throws RestResponseException {
	     
	    RestTemplate restTemplate = new RestTemplate();
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

	    try {
	   		R result = restTemplate.postForObject(uri, entity, returnedClass);	    	
		    return result;
	    } catch (HttpStatusCodeException e) {
	    	throw new RestResponseException(e.getResponseBodyAsString());
	    }
	}


	public U loginUser(U user) throws RestResponseException {
		return restPostCall(Util.getUri("/user/login"), user, clazz);
	}

	@SuppressWarnings("unchecked")
	public U get(String emailOid) throws RestResponseException {
		UUserAbs uu = restGetCall(Util.getUri("/user/" + emailOid), clazz);
		return (U) uu;
	}

	public U update(U user) throws RestResponseException {
		return restPostCall(Util.getUri("/user/update"), user, clazz);
	}

	public U add(U user) throws RestResponseException {
		return restPostCall(Util.getUri("/user/add"), user, clazz);
	}

	public U authinticateUser(U user) throws RestResponseException {
		return restPostCall(Util.getUri("/user/authinticate"), user, clazz);
	}

	public void updatePassword(U user) throws RestResponseException {
		restPostCall(Util.getUri("/user/update/password"), user, String.class);
	}

	public void resetPassword(UserUrl<U> userUrl) throws RestResponseException {
		restPostCall(Util.getUri("/user/reset/password"), userUrl, String.class);
	}


	public List<U> get(Collection<Long> ids) throws RestResponseException {
		return restPostCall(Util.getUri("/user/get/all"), ids, ArrayList.class);
	}

	public U get(long id) throws Exception {
		return get(id);
	}

	@Override
	public U update(Object obj) throws Exception {
		return update((U)obj);
	}
}
