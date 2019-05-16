package com.userSrvc.client.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

import org.springframework.stereotype.Component;

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

}
