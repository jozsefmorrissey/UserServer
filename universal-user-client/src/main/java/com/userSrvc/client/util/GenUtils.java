package com.userSrvc.client.util;

import java.net.URLEncoder;
import java.security.SecureRandom;

import com.goebl.david.Response;
import com.goebl.david.Webb;
import com.userSrvc.client.marker.HasId;

public class GenUtils {

	public static void main(String...args) {
		System.out.println(HasId.class.getName());
	}

	public static String randStringSecure(int length) {
		SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[length];
		random.nextBytes(bytes);
		String token = new String(bytes);
		return URLEncoder.encode(token);
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

	public static long hash(String str) {
		long hash = 7;
		for (int i = 0; i < str.length(); i++) {
		    hash = hash*31 + str.charAt(i);
		}		
		return hash;
	}
	
	public static long hash(HasId object) {
		return hash(object.getClass().getName() + ":" + object.getId());
	}
	public static long hash(String className, Long id) {
		return hash(className + ":" + id);
	}
}
