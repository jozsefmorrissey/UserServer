package com.userSrvc.client.util;

import java.nio.charset.Charset;
import java.util.Random;

import com.userSrvc.client.error.RestResponseException;

public class Util {
	
	public static void main(String...args) {
		System.out.println(randomString(50, "[a-zA-Z0-9@\\.]", "[a-zA-Z0-9]{1,}@[a-zA-Z0-9]{1,}\\.[a-zA-Z0-9]{1,}"));
	}
	
	private static final String domain = "http://localhost:7001/usvc";
	public static final String getUri(String endpoint) {
		return domain + endpoint;
	}
	
	public static String randomString(int length, String characterSetRegEx, String regEx) {
		String generatedString = "";
		while (!generatedString.matches(regEx)) {
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
