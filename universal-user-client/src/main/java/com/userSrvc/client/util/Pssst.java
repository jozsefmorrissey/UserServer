package com.userSrvc.client.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



public class Pssst {
	private static Map<String, JSONObject> objects = new HashMap<String, JSONObject>();

	public static void main(String...args) throws JSONException {
		String id = "usvc";
		System.out.println(get(id, "mailgun-api-key"));
		System.out.println(get(id, "mailgun-api-key"));
		System.out.println(get(id, "mailgun-api-key"));
		System.out.println(get(id, "mailgun-api-key"));
		System.out.println(get(id, "mailgun-api-key"));
		System.out.println(get(id, "token"));
	}
	
	public static String get(String id, String key) {
		if (objects.get(id) == null) {
			objects.put(id, fetch(id));
		}
		try {
			return objects.get(id).get(key).toString();
		} catch (JSONException e) {}
		return null;
	}
	
	private static JSONObject fetch(String id) {
		try {

			Process process = Runtime.getRuntime().exec("pst remote -config " + id);

			StringBuilder output = new StringBuilder();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				return new JSONObject(output.toString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
