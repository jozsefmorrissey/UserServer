package com.userSrvc.client.error;

import javax.annotation.ManagedBean;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

@ManagedBean
@ApplicationScope
@Data
public class RestResponseException extends Exception {
	String timestamp;
	String status;
	String error;
	String message;
	String path;
	
	public RestResponseException(String errorMsg) {
	    super(errorMsg);
	    JSONObject obj;
		try {
			obj = new JSONObject(errorMsg);
		    timestamp = obj.getString("timestamp");
		    status = obj.getString("status");
		    error = obj.getString("error");
		    message = obj.getString("message");
		    path = obj.getString("path");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
