package com.userSrvc.client.services;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;

import com.userSrvc.client.entities.ApplicationPermissionRequest;
import com.userSrvc.client.entities.Permission;

public interface PermissionSrvc {
	public List<Permission> get(long userId, long appId);
	public void add(ApplicationPermissionRequest pr) throws AccessDeniedException;
	public void remove(ApplicationPermissionRequest pr) throws AccessDeniedException;
	public void addAll(Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException;
	public void removeAll(Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException;
	public List<String> getTypes(long userId, long appId);
}
