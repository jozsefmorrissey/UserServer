package com.userSrvc.client.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.SrvcProps;

@Component
public class Util {
	public static void main(String...args) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
	}
		
	
	public static final String getUri(String endpoint) {
		return SrvcProps.getJsonMap().get("serviceRootUrl") + endpoint;
	}
	
	public static String randomString(int length, String characterSetRegEx, String regEx) {
		String generatedString = "";
		while (!generatedString.matches(regEx) || "".equals(generatedString)) {
			generatedString = "";
			for (int i = 0; i < length; i ++) {
				String character = "";
				while (character.length() != 1 || !character.matches(characterSetRegEx)) {
					byte[] single = new byte[1];
					new Random().nextBytes(single);
					character = new String(single, Charset.forName("UTF-8"));
				}
				generatedString += character;
			}
		}	 
	    return generatedString;
	}
	
	public static boolean responseExceptContains(RestResponseException e, String text) {
		return e.getMessage().indexOf(text) > -1;
	}
	
	public static <R> R restGetCall(String uri, Class<R> returnedClass) throws RestResponseException {
		RestTemplate rt= new RestTemplate();
	     
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

	    try {
	    	return rt.getForObject(uri, returnedClass);
	    } catch (HttpStatusCodeException e) {
	    	throw new RestResponseException(e.getResponseBodyAsString());
	    }
	}
	
	public static <R> R restPostCall(String uri, Object entity, Class<R> returnedClass) throws RestResponseException {
	     
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
	
	public static boolean notNullAndEqualOrBothNull(Object obj1, Object obj2) {
		return (obj1 != null && obj1.equals(obj2)) || (obj1 == null && obj2 == null);
	}
}
