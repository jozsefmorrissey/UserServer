package com.userSrvc.client.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import com.userSrvc.client.error.RestResponseException;

@Service
public class SrvcProps {
	@Value("${uu.propery.url:https://github.com/jozsefmorrissey/UserServer/raw/timeTrackerUserSrvc/Setup%20files/service-properties.json}")
	private String propertyUrl;

	private static Properties prop = new Properties();

	@PostConstruct
	public void setServiceProperties () throws RestResponseException {
	    try {
	   		JSONObject obj = new JSONObject(IOUtils.toString(new URL(propertyUrl), Charset.forName("UTF-8")));
	   		Iterator<?> it = obj.keys();
	   		while (it.hasNext()) {
	   			String key = it.next().toString();
	   			prop.setProperty(key, obj.getString(key));
	   			key = obj.keys().next().toString();
	   		}
	    } catch (HttpStatusCodeException e) {
	    	throw new RestResponseException(e.getResponseBodyAsString());
	    } catch (JSONException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Properties getProperties() {
		return prop;
	}
}
