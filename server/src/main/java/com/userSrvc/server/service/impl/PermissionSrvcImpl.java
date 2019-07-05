package com.userSrvc.server.service.impl;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.client.entities.ApplicationPermissionRequest;
import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.PermissionSrvc;
import com.userSrvc.server.repo.PermissionRepo;
import com.userSrvc.server.service.UserSrvc;

@Service
public class PermissionSrvcImpl
		implements PermissionSrvc {

	@Autowired
	UserSrvc userSrvc;
	
	@Autowired
	PermissionRepo permissionRepo;
	
	@Override
	public void add(ApplicationPermissionRequest pr) throws AccessDeniedException {
		garunteeApplicationOwnership(pr);
		cascade(pr);
		permissionRepo.save(pr.getPermission());
	}
	
	@Override
	public void addAll(Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException {
		for (ApplicationPermissionRequest pr : prs) {
			add(pr);
		}
	}

	@Override
	public void removeAll(Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException {
		for (ApplicationPermissionRequest pr : prs) {
			remove(pr);
		}
	}

	@Override
	public void remove(ApplicationPermissionRequest pr) throws AccessDeniedException {
		garunteeApplicationOwnership(pr);
		permissionRepo.delete(pr.getPermission());
	}

	private void cascade(ApplicationPermissionRequest pr) {
		if (pr.getParent() != null) {
			Permission neu = pr.getPermission();
			Permission old = pr.getParent();
			permissionRepo.cascade(neu.getRefType(), 
					neu.getRefId(), 
					old.getRefType(), 
					old.getRefId(), 
					pr.getApplication().getId(), 
					Permission.ADMIN);
		}
	}
	
	private void garunteeApplicationOwnership(ApplicationPermissionRequest pr) throws AccessDeniedException {
		userSrvc.authinticate((UUserAbs) pr.getApplication());
		pr.getPermission().setAppUserId(pr.getApplication().getId());
	}
}
