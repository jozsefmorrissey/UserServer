package com.userSrvc.client.services;

import java.util.List;

import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.DatabaseIntegrityException;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.marker.HasType;

public interface PermissionSrvcExt <U extends UUserAbs> {
	public void add(U user, HasType hasType, Permission parent) throws DatabaseIntegrityException, RestResponseException;
	public void grant(U from, U to, List<Permission> permission) throws RestResponseException, Exception;
	public void remove(U admin, U from, List<Permission> permissions) throws RestResponseException, Exception;
	public void transfer(U admin, U from, U to, List<Permission> permission) throws RestResponseException, Exception;
	public U getApplicationUser();
	public void clone(U admin, U from, U to) throws Exception;
	
	public Permission build(HasType hasId);
	public void isAuthorized(UUserAbs user, HasType hasId);
	public Permission build(UUserAbs user, HasType hasId);
}
