package com.userSrvc.server.utils;

import java.security.SecureRandom;

import com.goebl.david.Response;
import com.goebl.david.Webb;

public class GenUtils {

	public static void main(String...args) {
		System.out.println("'" + getPassword("dbPass", "ohtaemouKiDeequohshiengoopae0o", "8040") + "'");
	}

	public static String randStringSecure(int length) {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[length];
		random.nextBytes(bytes);
		String token = bytes.toString();
		return token;
	}

	public static String getPassword(String identifier, String token, String port) {
		System.out.println(identifier + ":" + token + ":" + port);
		Webb webb = Webb.create();
		Response<String> req = webb.post("http://localhost:" + port + "/password/get")
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
