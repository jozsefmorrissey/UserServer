package com.userSrvc.client.services;

import java.util.List;

import com.userSrvc.client.entities.access.ObjectAccess;
import com.userSrvc.client.marker.HasType;

public interface AopAccess <A extends ObjectAccess<?>> {
	public Boolean hasAccess(HasType hasType,
			List<List<ObjectAccess<A>>> objectAccessLists);
}
