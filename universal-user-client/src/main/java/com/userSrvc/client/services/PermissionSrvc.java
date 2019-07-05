package com.userSrvc.client.services;

import java.nio.file.AccessDeniedException;
import java.util.Collection;

import com.userSrvc.client.entities.ApplicationPermissionRequest;

public interface PermissionSrvc {
	public void add(ApplicationPermissionRequest pr) throws AccessDeniedException;
	public void remove(ApplicationPermissionRequest pr) throws AccessDeniedException;
	void addAll(Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException;
	void removeAll(Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException;
}
