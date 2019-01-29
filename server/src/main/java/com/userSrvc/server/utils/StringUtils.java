package com.userSrvc.server.utils;

import java.util.HashMap;
import java.util.Set;

public class StringUtils {
	public static String replaceKeyValues(String str, HashMap<String, Object> keyValues, String prefix, String suffix) {
		Set<String> keys = keyValues.keySet();
		for (String key : keys) {
			String target = prefix + key + suffix;
			String replacement = keyValues.get(key).toString();
			str = str.replace(target, replacement);
		}
		
		return str;
	}
	
	public static String replaceKeyValues(String str, HashMap<String, Object> keyValues) {
		return replaceKeyValues(str, keyValues, "", "");
	}
	
	public static String cleanHtml(String input) {
		return input.replaceAll("<script>.*?</script>", "");
	}
}
