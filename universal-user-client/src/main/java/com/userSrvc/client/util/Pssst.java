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
			if (objects.get(id) != null) {
				return objects.get(id).get(key).toString();				
			}
		} catch (JSONException e) {}
		return null;
	}
	
	private static JSONObject fetch(String id) {
		try {

			String[] cmd = new String[] {"pst", "remote", "-config", id, "2>/dev/null"};
			Process process = Runtime.getRuntime().exec(cmd);

			StringBuilder output = new StringBuilder();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));

			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}

			int exitVal = process.waitFor();
			if (exitVal == 0) {
				System.out.println(output.toString());
				System.out.println("<-out|json->");
				System.out.println(new JSONObject(output.toString()));
				return new JSONObject(output.toString());
			}
			throw new PssstFetchFailedException(id);
		} catch (Exception e) {
			new DebugGui(true).exception("Pssst.fetch", id, new PssstFetchFailedException(id));
			return null;
		}
	}
	
	public static class PssstFetchFailedException extends Exception {
		private static final long serialVersionUID = 7749383507428066033L;
		private String id;
		
		public PssstFetchFailedException(String id) {
			this.id = id;
		}

		@Override
		public String getMessage() {
			return "You should ensure your configuration for '" + id + "' is correct.\n\t" + 
					"pst remote -config '" + id + "'\n\tShould return a valid json string.";
		}
	}
}
