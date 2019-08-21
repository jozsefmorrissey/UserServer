package com.userSrvc.client.services;

import java.util.Collection;
import java.util.List;

import com.userSrvc.client.entities.ApplicationPermissionRequest;
import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.entities.UUserAbs;

public interface PermissionSrvc <U extends UUserAbs> {
	public List<Permission> get(long userId, long appId);
	public void add(ApplicationPermissionRequest<U> pr) throws Exception;
	public void remove(ApplicationPermissionRequest<U> pr) throws Exception;
	public void addAll(Collection<ApplicationPermissionRequest<U>> prs) throws Exception;
	public void removeAll(Collection<ApplicationPermissionRequest<U>> prs) throws Exception;
	public List<String> getTypes(long userId, long appId);
}
