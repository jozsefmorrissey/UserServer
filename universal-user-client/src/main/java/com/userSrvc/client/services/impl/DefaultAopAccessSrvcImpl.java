package com.userSrvc.client.services.impl;

import java.lang.reflect.AnnotatedType;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.userSrvc.client.annotation.AopAccessNameOnly;
import com.userSrvc.client.entities.access.ObjectAccess;
import com.userSrvc.client.marker.HasType;
import com.userSrvc.client.repo.AccessObjectBaseRepository;
import com.userSrvc.client.services.AopAccess;

public class DefaultAopAccessSrvcImpl implements AopAccess<ObjectAccess<?>> {
	
	@Autowired
	AccessObjectBaseRepository<ObjectAccess<?>>[] repos;
	
	@Autowired
	AopAccess<?>[] aopAccessSrvcList;

	List<AopAccess<?>> defaultAccessors;
	HashMap<String, AopAccess<?>> byFqClassAccessors;

	@PostConstruct
	private void parseRepos() {
		for (AopAccess<?> accessSrvc : aopAccessSrvcList) {
			boolean isNameOnly = false;
			for (AnnotatedType anno : accessSrvc.getClass().getAnnotatedInterfaces()) {
				if (anno instanceof AopAccessNameOnly) {
					isNameOnly = true;
				}
			}
			if (!isNameOnly) {
				defaultAccessors.add(accessSrvc);
			}
			String canName = accessSrvc.getClass().getCanonicalName();
			byFqClassAccessors.put(canName, accessSrvc);
		}
	}

	@Override
	public Boolean hasAccess(HasType hasType, List<List<ObjectAccess<ObjectAccess<?>>>> objectAccessLists) {
		// TODO Auto-generated method stub
		return null;
	}

}
