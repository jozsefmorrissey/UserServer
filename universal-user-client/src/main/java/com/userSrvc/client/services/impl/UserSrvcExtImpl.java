package com.userSrvc.client.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

public class UserSrvcExtImpl<U extends UUserAbs> implements UserSrvcExt<U> {

	private static <E,R> R restCall(String uri, E entity, R returned) throws RestResponseException {
	     
	    RestTemplate restTemplate = new RestTemplate();
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

	    try {
	   		R result = (R) restTemplate.postForObject(uri, entity, returned.getClass());	    	
		    return result;
	    } catch (HttpStatusCodeException e) {
	    	throw new RestResponseException(e.getResponseBodyAsString());
	    }
	}


	public U loginUser(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/login"), user, user);
	}

	public U get(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/get"), user, user);
	}

	public U update(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/update"), user, user);
	}

	public U add(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/add"), user, user);
	}

	public U authinticateUser(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/authinticate"), user, user);
	}

	public void updatePassword(U user) throws RestResponseException {
		restCall(Util.getUri("/user/update/password"), user, "");
	}

	public void resetPassword(UserUrl<U> userUrl) throws RestResponseException {
		restCall(Util.getUri("/user/reset/password"), userUrl, "");
	}


	public List<U> getAll(Collection<Long> ids) throws RestResponseException {
		return restCall(Util.getUri("/user/get/all"), ids, new ArrayList());
	}
}
