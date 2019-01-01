package com.userSrvc.server.utils;

import java.security.SecureRandom;

import com.goebl.david.Response;
import com.goebl.david.Webb;

public class GenUtils {

	public static void main(String...args) {
		System.out.println("'" + getPassword("dbpass", "Chais6shinae6Ibeik6uam5ShahJa") + "'");
	}

	public static String randStringSecure(int length) {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[length];
		random.nextBytes(bytes);
		String token = bytes.toString();
		return token;
	}

	public static String getPassword(String identifier, String token) {
		Webb webb = Webb.create();
		Response<String> req = webb.post("http://localhost:8080/password/get")
		        .param("collectionIdentifier", "UserSrvc")
		        .param("passwordIdentifier", identifier)
		        .param("token", token)
		        .ensureSuccess()
		        .asString();

		String password = req.getBody();
//		password = password.substring(password.length() - 3);

		return password;
	}

}
