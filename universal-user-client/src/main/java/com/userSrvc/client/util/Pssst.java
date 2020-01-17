package com.userSrvc.client.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



public class Pssst {
	private static Map<String, JSONObject> objects = new HashMap<String, JSONObject>();
	
	public static String get(String id, String key) {
		DebugGui.log("getting: " + id + "/" + key);
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
		System.out.println("fetching: " + id );
		try {
			
//			String[] cmd = new String[] {"pst", "remote", "-config", id};
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

			throw new PssstFetchFailedException(id, output.toString());
		} catch (Exception e) {
			DebugGui.log("Pssst.fetch - exception: " + e.getMessage());
			DebugGui.exception("Pssst.fetch", id, e);
			return null;
		}
	}
	
	public static class PssstFetchFailedException extends Exception {
		private static final long serialVersionUID = 7749383507428066033L;
		private String id;
		private String output;
		
		public PssstFetchFailedException(String id, String output) {
			this.id = id;
			this.output = output;
		}

		@Override
		public String getMessage() {
			return "You should ensure your configuration for '" + id + "' is correct.\n\t" + 
					"pst remote -config '" + id + "'\n\tShould return a valid json string." + 
					"\n\n\tOutupt Recieved:" + output;
		}
	}
}
