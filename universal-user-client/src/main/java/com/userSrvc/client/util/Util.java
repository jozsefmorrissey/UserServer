package com.userSrvc.client.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.SrvcProps;

@Component
public class Util {
	private final static ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

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
	
	public static <R> R restGetCall(String uri, Class<R> returnedClass,
			MultiValueMap<String, String> headers) throws RestResponseException {
		RestTemplate rt= new RestTemplate();
	     
	    HttpHeaders httpHeaders = new HttpHeaders();
	    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    httpHeaders.addAll(headers);

	    HttpEntity<Object> request = new HttpEntity<Object>(null, httpHeaders);
	    try {
	    	ResponseEntity<R> respEntity = rt.exchange(uri, HttpMethod.GET, request, returnedClass);
	    	return respEntity.getBody();
	    } catch (HttpStatusCodeException e) {
	    	throw new RestResponseException(e.getResponseBodyAsString());
	    }
	}
	
	public static <R> R restPostCall(String uri, Object obj, Class<R> returnedClass,
			MultiValueMap<String, String> headers) throws RestResponseException {
	     
	    RestTemplate restTemplate = new RestTemplate();
	    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
	    
	    HttpHeaders httpHeaders = new HttpHeaders();
	    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    httpHeaders.addAll(headers);

	    HttpEntity<Object> request = new HttpEntity<Object>(obj, httpHeaders);
	    try {
	   		R result = restTemplate.postForObject(uri, request, returnedClass);	    	
		    return result;
	    } catch (HttpStatusCodeException e) {
	    	throw new RestResponseException(e.getResponseBodyAsString());
	    }
	}
	
	public static boolean notNullAndEqualOrBothNull(Object obj1, Object obj2) {
		return (obj1 != null && obj1.equals(obj2)) || (obj1 == null && obj2 == null);
	}
	
	public static <U> List<U> convertMapListToObjects(List<Map> maps, Class<U> clazz) {
		List<U> list = new ArrayList<U>();
		for (Map map : maps) {
			list.add(mapper.convertValue(map, clazz));
		}
		return list;
	}

}
