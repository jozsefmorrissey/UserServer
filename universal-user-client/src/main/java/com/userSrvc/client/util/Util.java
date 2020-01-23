package com.userSrvc.client.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.marker.HasAddField;
import com.userSrvc.client.services.SrvcProps;

@Component
public class Util {
	private final static ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
	private final static Gson gson = new Gson();

	public static void main(String...args) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
	}
	
	public static <T> T notNull(T...objects) {
		for (T obj : objects) {
			if (obj != null) {
				return obj;
			}
		}
		return null;
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
		return restGetCall(uri, returnedClass, new HttpHeaders());
	}
	
	public static <R> R restGetCall(String uri, Class<R> returnedClass,
			MultiValueMap<String, String> headers) throws RestResponseException {
/*		AopAuth.getBean().getCurrentDebugGui()
		.value("utils.restPostCall." + callId, "uri", uri)
		.value("utils.restPostCall." + callId, "returnedClass", returnedClass)
		.value("utils.restPostCall." + callId, "headers", headers);
*/
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
	
	public static <R> R restPostCall(String uri, Object obj, Class<R> returnedClass) throws RestResponseException {
		return restPostCall(uri, obj, returnedClass, new HttpHeaders());
	}
	
	public static <R> R restPostCall(String uri, Object obj, Class<R> returnedClass,
			MultiValueMap<String, String> headers) throws RestResponseException {

/*		AopAuth.getBean().getCurrentDebugGui()
			.value("utils.restPostCall." + callId, "uri", uri)
			.value("utils.restPostCall." + callId, "obj", obj)
			.value("utils.restPostCall." + callId, "returnedClass", returnedClass)
			.value("utils.restPostCall." + callId, "headers", headers);
*/		
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
	    	throw new RestClientResponseException(e.getMessage(), 
	    				e.getRawStatusCode(), 
	    				e.getStatusText(), 
	    				e.getResponseHeaders(), 
	    				e.getResponseBodyAsByteArray(), 
	    				Charset.defaultCharset());
	    }
	}
	
	public static boolean notNullAndEqualOrBothNull(Object obj1, Object obj2) {
		return (obj1 != null && obj1.equals(obj2)) || (obj1 == null && obj2 == null);
	}
	
	public static <U> List<U> convertMapListToObjects(List<Map<?,?>> maps, Class<U> clazz) {
		List<U> list = new ArrayList<U>();
		for (Map<?,?> map : maps) {
			list.add(mapper.convertValue(map, clazz));
		}
		return list;
	}

	public static String toJson(Object obj) {
	      ObjectMapper mapper = new ObjectMapper();
	      try
	      {
	         return mapper.writeValueAsString(obj);
	      } catch (JsonGenerationException e)
	      {
	         e.printStackTrace();
	      } catch (JsonMappingException e)
	      {
	         e.printStackTrace();
	      } catch (IOException e)
	      {
	         e.printStackTrace();
	      }
	      return "{}";
	}
	
	public static JsonObject toJsonObj(String jsonStr) {
		return gson.fromJson(jsonStr, JsonObject.class);
	}
	
	public static Cookie getCookie(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}
	
	public static Boolean strExists(String needle, JsonArray jsonArray) {
		for (JsonElement jsonElement : jsonArray) {
			if (notNullAndEqualOrBothNull(jsonElement.getAsString(), needle)) {
				return true;
			}
		}
		return false;
	}
	
	public static JSONObject toJson(Enum<?> enumInst) {
		Field[] fields = enumInst.getClass().getFields();
		JSONObject jsonObj = null;
		jsonObj = new JSONObject();
		
		boolean checkField = enumInst instanceof HasAddField;
		HasAddField hasAddField = null;
	
		if (checkField) {
			hasAddField = ((HasAddField) enumInst);
		}
		
		for (Field field : fields) {
			if (!checkField || 
					hasAddField.addField(field.getName().toString())) {
				String[] path = field.getName().split("_");
				JSONObject curr = jsonObj;
				try {
					for (String id : path) {
						if (!curr.has(id)) {
							curr.putOpt(id, new JSONObject());
						}
						curr = (JSONObject) curr.get(id);
					}
					Object fieldValue = field.get(enumInst);
					if (fieldValue == null) {
						curr.accumulate("value", null);
					} else if (fieldValue.getClass().isArray()) {
						curr.accumulate("value", fieldValue);
					} else {
						curr.accumulate("value", fieldValue.toString());
					}
				} catch (IllegalArgumentException | IllegalAccessException | JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObj;
	}
}
