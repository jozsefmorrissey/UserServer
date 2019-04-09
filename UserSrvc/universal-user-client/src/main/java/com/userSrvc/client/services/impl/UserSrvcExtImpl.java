package com.userSrvc.client.services.impl;

import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.userSrvc.client.entities.EmailToken;
import com.userSrvc.client.entities.UUser;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

@Service
public class UserSrvcExtImpl implements UserSrvcExt {

	public static void main(String...args) throws Exception {
		String uri = Util.getUri("/user/get");
	    EmailToken entity = new EmailToken("dapibus@in.net", "[B@d077e31");

//		System.out.println(restCall(uri, entity, new UUser()));
		
	    UUser user = new UUser(0, null, "jozsef.morrissey@gmail.com", "password");
	    user.setUserToken("[B@d077e31");

//		uri = getUri("/user/update/password");
//		System.out.println(restCall(uri, user, new UUser()));

		user.setUserToken(null);
		uri = Util.getUri("/user/login");
		System.out.println(restCall(uri, user, new UUser()));

	}
	
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


	public <U extends UUser> U loginUser(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/login"), user, user);
	}

	public <U extends UUser> U getUser(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/get"), user, user);
	}

	public <U extends UUser> U update(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/update"), user, user);
	}

	public <U extends UUser> U add(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/add"), user, user);
	}

	public <U extends UUser> U authinticateUser(U user) throws RestResponseException {
		return restCall(Util.getUri("/user/authinticate"), user, user);
	}

	public <U extends UUser> void updatePassword(U user) throws RestResponseException {
		restCall(Util.getUri("/user/update/password"), user, "");
	}

	public <U extends UUser> void resetPassword(UserUrl userUrl) throws RestResponseException {
		restCall(Util.getUri("/user/reset/password"), userUrl, "");
	}
}
