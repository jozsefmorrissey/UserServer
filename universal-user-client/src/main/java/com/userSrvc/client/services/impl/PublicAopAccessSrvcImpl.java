package com.userSrvc.client.services.impl;

import java.util.List;

import com.userSrvc.client.entities.access.AppObjectAccess;
import com.userSrvc.client.entities.access.ObjectAccess;
import com.userSrvc.client.marker.HasType;
import com.userSrvc.client.services.AopAccess;

public class PublicAopAccessSrvcImpl implements AopAccess<AppObjectAccess> {

//	@Override
//	public Boolean hasAccess(HasType hasType, List<List<ObjectAccess<?>>> objectAccessLists) {
//		for (List<ObjectAccess<?>> list : objectAccessLists) {
//			if (list.size() > 0 && 
//					list.get(0) instanceof AppObjectAccess &&
//					0l == list.get(0).getId()) {
//				
//			}
//		}
//		return null;
//	}

	@Override
	public Boolean hasAccess(HasType hasType, List<List<ObjectAccess<AppObjectAccess>>> objectAccessLists) {
		// TODO Auto-generated method stub
		return null;
	}

}
