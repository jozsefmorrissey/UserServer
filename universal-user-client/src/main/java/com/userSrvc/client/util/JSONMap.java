package com.userSrvc.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMap {
	
	HashMap<String, Object> hashMap = new HashMap<String, Object>();
	
	public JSONMap(String json) {
		buildMap(json, "");
	}
	
	private Object buildMap(String str, String prefix) {
		try {
	   		JSONObject obj = new JSONObject(str);
	   		Iterator<?> it = obj.keys();
	   		while (it.hasNext()) {
	   			String key = it.next().toString();
	   			String currPrefix = prefix.length() > 0 ? prefix + "." + key : key;
	   			Object curr = obj.get(key);
	   			if (curr.getClass().equals(JSONArray.class)) {
	   				JSONArray arr = (JSONArray)curr;
	   				List<Object> list = new ArrayList<Object>();
	   				for (int index = 0; index < arr.length(); index += 1) {
	   					String listPrefix = currPrefix + "[" + index + "]";
	   					String listObj = arr.get(index).toString();
	   					list.add(buildMap(listObj, listPrefix));
			   			hashMap.put(listPrefix, listObj);	
	   				}
		   			hashMap.put(currPrefix, list);	
	   			} else {
		   			hashMap.put(currPrefix, curr);
	   			}
	   			buildMap(obj.getString(key), currPrefix);
	   		}
	    } catch (JSONException e) {
	    }
		return str;
	}
	
	public Object get(String id) {
		return hashMap.get(id);
	}
	
	public List<String> getKeys() {
		return getKeys(".*");
	}

	public List<String> getKeys(String regex) {
		List<String> keys = new ArrayList<String>();
		Set<String> keySet = hashMap.keySet();
		for (String key : keySet) {
			if (key.matches(regex)) {
				keys.add(key);
			}
		}
		return keys;
	}
}
