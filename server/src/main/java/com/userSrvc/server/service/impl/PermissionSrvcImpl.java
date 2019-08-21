package com.userSrvc.server.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.client.entities.ApplicationPermissionRequest;
import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.PermissionSrvc;
import com.userSrvc.server.repo.PermissionRepo;

@Service
public class PermissionSrvcImpl
		implements PermissionSrvc<UUserAbs> {

	@Autowired
	UserSrvcImpl userSrvc;
	
	@Autowired
	PermissionRepo permissionRepo;
	
	@Override
	public void add(ApplicationPermissionRequest<UUserAbs> pr) throws Exception {
		guaranteeApplicationOwnership(pr);
		cascade(pr);
		permissionRepo.save(pr.getPermission());
	}
	
	@Override
	public void addAll(Collection<ApplicationPermissionRequest<UUserAbs>> prs) throws Exception {
		for (ApplicationPermissionRequest<UUserAbs> pr : prs) {
			add(pr);
		}
	}

	@Override
	public void removeAll(Collection<ApplicationPermissionRequest<UUserAbs>> prs) throws Exception {
		for (ApplicationPermissionRequest<UUserAbs> pr : prs) {
			remove(pr);
		}
	}

	@Override
	public void remove(ApplicationPermissionRequest<UUserAbs> pr) throws Exception {
		guaranteeApplicationOwnership(pr);
		permissionRepo.delete(pr.getPermission());
	}

	private void cascade(ApplicationPermissionRequest<UUserAbs> pr) {
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
	
	private void guaranteeApplicationOwnership(ApplicationPermissionRequest<UUserAbs> pr) throws Exception {
		userSrvc.authinticate((UUserAbs) pr.getApplication());
		pr.getPermission().setAppUserId(pr.getApplication().getId());
	}

	@Override
	public List<Permission> get(long userId, long appId) {
		return permissionRepo.getAllByUserIdAndAppUserId(userId, appId);
	}

	@Override
	public List<String> getTypes(long userId, long appId) {
		List<String> types = new ArrayList<String>();
		List<Permission> perms = permissionRepo.findDistinct(userId, appId);
		for (Permission perm : perms) {
			types.add(perm.getRefType());
			System.out.println(perm);
		}
		return types;
	}
}
