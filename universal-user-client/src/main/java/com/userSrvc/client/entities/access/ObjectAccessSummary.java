package com.userSrvc.client.entities.access;

import java.util.HashMap;
import java.util.Map;

public class ObjectAccessSummary {
	private Map<String, Boolean> fieldMap = new HashMap<String, Boolean>();
	
	public void updateAccess(String fieldName, Boolean canAccess) {
		if (canAccess == null) {
			return;
		}
		
		if (canAccess == false) {
			fieldMap.put(fieldName, false);
		} else {
			if (false != fieldMap.get(fieldName)) {
				fieldMap.put(fieldName, true);
			}
		}
	}
	
	public boolean hasAccess(String fieldName) {
		return true == fieldMap.get(fieldName);
	}
}
