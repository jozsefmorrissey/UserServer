package com.userSrvc.client.constant;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class ToJson {
	
	public boolean addField(String name) {
		return name.matches("(TOKEN|PASSWORD|EMAIL)");
	}
	
	public JSONObject toJson() {
		Field[] fields = this.getClass().getFields();
		JSONObject jsonObj = null;
		jsonObj = new JSONObject();
		for (Field field : fields) {
			if (addField(field.getName())) {
				String[] path = field.getName().split("_");
				JSONObject curr = jsonObj;
				try {
					for (String id : path) {
						if (!curr.has(id)) {
							curr.putOpt(id, new JSONObject());
						}
						curr = (JSONObject) curr.get(id);
					}
					curr.accumulate("value", field.get(this));
				} catch (IllegalArgumentException | IllegalAccessException | JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return jsonObj;
	}
	
	public String toString() {
		JSONObject jsonObj = toJson();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonParser jp = new JsonParser();
		JsonElement je = jp.parse(jsonObj.toString());
		return gson.toJson(je);
	}
	
	public List<String> values() {
		Field[] fields = this.getClass().getDeclaredFields();
		List<String> values = new ArrayList<String>();
		for (Field field : fields) {
			try {
				if (field.getModifiers() == 0) {
					values.add((String) field.get(this));
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return values;
	}
}
