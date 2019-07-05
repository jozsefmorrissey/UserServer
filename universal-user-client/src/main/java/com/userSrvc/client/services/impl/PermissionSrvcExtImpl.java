package com.userSrvc.client.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.ApplicationPermissionRequest;
import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.DatabaseIntegrityException;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.marker.HasType;
import com.userSrvc.client.services.PermissionSrvcExt;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

public abstract class PermissionSrvcExtImpl <U extends UUserAbs> implements PermissionSrvcExt<U> {

	@Autowired
	private UserSrvcExt<U> userSrvc;
	
	@Override
	public void add(U user, HasType hasType, Permission parent) throws DatabaseIntegrityException, RestResponseException {
		if (hasType == null) {
			throw new DatabaseIntegrityException("refType must be defined");
		}

		Permission perm = new Permission(null, this.getApplicationUser().getId(), 
					user.getId(), hasType.getObjectType(), hasType.getId(), Permission.OAO, null, null);
		
		ApplicationPermissionRequest pq = new ApplicationPermissionRequest();
		pq.setApplication(getApplicationUser());
		pq.setPermission(perm);
		pq.setParent(parent);
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD), pq, String.class);
	}

	@Override
	public void grant(U from, U to, List<Permission> permissions) throws Exception {
		userSrvc.authinticateUser(from);
		U dbUser = userSrvc.get(from.getId());
		Collection<ApplicationPermissionRequest> permReqs = grantPermissions(dbUser, permissions, to);
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD_ALL), permReqs, String.class);
	}

	@Override
	public void remove(U admin, U from, List<Permission> permissions) throws Exception {
		admin = userSrvc.authinticateUser(admin);
		U dbUser = userSrvc.get(from.getId());
		Collection<ApplicationPermissionRequest> permReqs = removePermissions(admin, permissions);
		Util.restPostCall(Util.getUri(URI.PERMISSION_REMOVE_ALL), permReqs, String.class);
	}

	@Override
	public void transfer(U admin, U from, U to, List<Permission> permissions) throws Exception {
		userSrvc.authinticateUser(from);
		U dbUser = userSrvc.get(from.getId());
		Collection<ApplicationPermissionRequest> transReqs = transferPermissions(dbUser, permissions, to);
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD_ALL), transReqs, String.class);
	}
	
	@Override
	public void clone(U admin, U from, U to) throws Exception {
		admin = userSrvc.authinticateUser(admin);
		U dbUser = userSrvc.get(from.getId());
		Collection<ApplicationPermissionRequest> permReqs = clonePermissions(admin, dbUser.getPermissions(), to);
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD_ALL), permReqs, String.class);
	}
	
	@Override
	public Permission build(HasType hasType) {
		return new Permission(null, this.getApplicationUser().getId(), 
				null, hasType.getObjectType(), hasType.getId(), null, null, null);
	}

	@Override
	public Permission build(UUserAbs user, HasType hasType) {
		return new Permission(null, this.getApplicationUser().getId(), 
				user.getId(), hasType.getObjectType(), hasType.getId(), Permission.OAO, 
				user.getId(), null);
	}

	@Override
	public void isAuthorized(UUserAbs user, HasType hasType) {
		List<Permission> permissions = user.getPermissions();
		for (Permission perm : permissions) {
			if (perm.getRefType().equals(hasType.getObjectType())) {
				if (perm.getType().equals(Permission.ADMIN) ||
						perm.getType().equals(Permission.OAO) ||
						perm.getType().equals(Permission.PSUEDO)) {
					return;
				} else if (perm.getType().equals(Permission.VALIDATION)) {
					// TODO: Implement send email request
					throw new ValidationException("This user requires validation to modify this object, an email has been sent to an authority");
				}
			} else if (perm.equals(Permission.ROOT)) {
				return;
			}
		}
	}

	protected Collection<ApplicationPermissionRequest> grantPermissions(U dbUser, Collection<Permission> perms, U to) {
		List<Permission> userPerms = dbUser.getPermissions();
		List<ApplicationPermissionRequest> permReqs = new ArrayList<ApplicationPermissionRequest>();
		for (Permission perm : perms) {
			boolean found = false;
			for (Permission uPerm : userPerms) {
				boolean eq = uPerm.equals(perm);
				boolean isAdminOoao = isType(uPerm, Permission.ADMIN, Permission.OAO);
				boolean isPsuedoOvalidation = isType(uPerm, Permission.PSUEDO, Permission.VALIDATION);
				if (eq && isAdminOoao) {
					found = true;

					perm.setGrantedFromUserId(dbUser.getId());
					ApplicationPermissionRequest apq = createNewRequest(dbUser, to, perm, false);
					permReqs.add(apq);
					
					break;
				} else if (uPerm.equals(perm) && isPsuedoOvalidation) {
					// TODO: add pending table in db and email validation from OAO
					found = true;
					throw new DataIntegrityViolationException("You havent implemented this functionality yet");
				}
			}

			if (!found) {
				throw new DataIntegrityViolationException("Attempting to modify permission by an unauthorized user.");
			}
		}
		return permReqs;
	}

	protected Collection<ApplicationPermissionRequest> removePermissions(U dbUser, Collection<Permission> perms) {
		List<Permission> userPerms = dbUser.getPermissions();
		List<ApplicationPermissionRequest> permReqs = new ArrayList<ApplicationPermissionRequest>();
		for (Permission perm : perms) {
			boolean found = false;
			for (Permission uPerm : userPerms) {
				if (uPerm == null) {
					break;
				}
				boolean eq = uPerm.equals(perm);
				boolean originAdmin = isType(uPerm, Permission.OAO, Permission.ADMIN);
				boolean psuedo = uPerm.getType().equals(Permission.PSUEDO);
				boolean otherPsuedo = perm.getType().equals(Permission.PSUEDO);
				boolean otherNotOriginOrPsuedo = !isType(perm, Permission.OAO, Permission.PSUEDO);
				boolean originOpsudeoAndOtherNot = originAdmin || (psuedo  && otherNotOriginOrPsuedo);
				if ( eq && originOpsudeoAndOtherNot) {
					found = true;
					
					ApplicationPermissionRequest apq = new ApplicationPermissionRequest();
					apq.setApplication(getApplicationUser());
					apq.setPermission(perm);
					permReqs.add(apq);
					
					break;
				} else if (eq && psuedo  && otherPsuedo) {
					// TODO: add pending table in db and email validation from OAO
					found = true;
					throw new DataIntegrityViolationException("You havent implemented this functionality yet");
				}
			}

			if (!found) {
				throw new DataIntegrityViolationException("Attempting to modify permission by an unauthorized user.");
			}
		}
		return permReqs;
	}
	
	protected Collection<ApplicationPermissionRequest> transferPermissions(U dbUser, Collection<Permission> perms, U to) {
		List<Permission> userPerms = dbUser.getPermissions();
		Collection<ApplicationPermissionRequest> permReqs = new ArrayList<ApplicationPermissionRequest>();
		for (Permission perm : perms) {
			boolean found = false;
			for (Permission uPerm : userPerms) {
				if (uPerm == null) {
					break;
				}

				boolean eq = uPerm.equals(perm);
				boolean isAdminOoao = isType(uPerm, Permission.ADMIN, Permission.OAO);
				if (eq && isAdminOoao) {
					found = true;
					
					ApplicationPermissionRequest apq = createNewRequest(dbUser, to, uPerm, true);
					permReqs.add(apq);

					break;
				}
			}

			if (!found) {
				throw new DataIntegrityViolationException("Attempting to modify permission by an unauthorized user.");
			}
		}
		return permReqs;
	}
	
	protected Collection<ApplicationPermissionRequest> clonePermissions(U dbUser, Collection<Permission> perms, U to) {
		List<Permission> userPerms = dbUser.getPermissions();
		List<ApplicationPermissionRequest> permReqs = new ArrayList<ApplicationPermissionRequest>();
		for (Permission perm : perms) {
			boolean found = false;
			for (Permission uPerm : userPerms) {
				if (uPerm == null) {
					break;
				}

				if (uPerm.equals(perm)) {
					found = true;
					
					ApplicationPermissionRequest apq = createNewRequest(dbUser, to, perm, false);
					permReqs.add(apq);
					
					break;
				}
			}

			if (!found) {
				throw new DataIntegrityViolationException("Attempting to modify permission by an unauthorized user.");
			}
		}
		return permReqs;
	}
	
	protected ApplicationPermissionRequest createNewRequest(U from, U to, Permission perm, boolean isTransfer) {
		perm.setUserId(to.getId());
		if (!isTransfer) {
			perm.setId(null);
			if (perm.getType().equals(Permission.OAO)) {
				perm.setType(Permission.PSUEDO);
			}
		}
		
		ApplicationPermissionRequest apq = new ApplicationPermissionRequest();
		apq.setApplication(getApplicationUser());
		apq.setPermission(perm);
	
		return apq;
	}
	
	public boolean isType(Permission perm, String...types) {
		if (perm.getType() == null) {
			return false;
		}
		
		for (String type : types) {
			if (perm.getType().equals(type)) {
				return true;
			}
		}
		
		return false;
	}
}
