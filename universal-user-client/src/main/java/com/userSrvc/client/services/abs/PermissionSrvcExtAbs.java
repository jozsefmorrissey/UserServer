package com.userSrvc.client.services.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.ApplicationPermissionRequest;
import com.userSrvc.client.entities.ConnectionState;
import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.DatabaseIntegrityException;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.marker.HasType;
import com.userSrvc.client.services.PermissionSrvcExt;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

public abstract class PermissionSrvcExtAbs <U extends UUserAbs> implements PermissionSrvcExt<U> {

	@Autowired
	AopAuth<U, ?> aopAuth;
	
	public abstract UserSrvcExt<U> getUserSrvc();
	
	@Override
	public void add(U user, HasType hasType, Permission parent) throws DatabaseIntegrityException, RestResponseException {
		if (hasType == null) {
			throw new DatabaseIntegrityException("refType must be defined");
		}

		Permission perm = new Permission(null, this.getApplicationUser().getId(), 
					user.getId(), hasType.getObjectType(), hasType.getId(), Permission.OAO, null, null);
		
		ApplicationPermissionRequest<U> pq = new ApplicationPermissionRequest<U>();
		pq.setApplication(getApplicationUser());
		pq.setPermission(perm);
		pq.setParent(parent);
		
		MultiValueMap<String, String> headers = new HttpHeaders();
		headers.add(ConnectionState.EMAIL, aopAuth.getCurrentUser().getEmail());
		headers.add(ConnectionState.TOKEN, aopAuth.getCurrentUser().getToken().getToken());
		headers.add(ConnectionState.PASSWORD, aopAuth.getCurrentUser().getPassword());
		
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD), pq, String.class, aopAuth.getHeaders());
	}

	@Override
	public void grant(U to, List<Permission> permissions) throws Exception {
		U from = getUserSrvc().authinticate();
		U dbUser = getUserSrvc().get(from.getEmail());
		Collection<ApplicationPermissionRequest<U>> permReqs = grantPermissions(dbUser, permissions, to);
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD_ALL), permReqs, String.class, aopAuth.getHeaders());
	}

	@Override
	public void remove(U from, List<Permission> permissions) throws Exception {
		U admin = getUserSrvc().authinticate();
		Collection<ApplicationPermissionRequest<U>> permReqs = removePermissions(admin, permissions);
		Util.restPostCall(Util.getUri(URI.PERMISSION_REMOVE_ALL), permReqs, String.class, aopAuth.getHeaders());
	}

	@Override
	public void transfer(U from, U to, List<Permission> permissions) throws Exception {
		U dbUser = getUserSrvc().get(from.getEmail());
		Collection<ApplicationPermissionRequest<U>> transReqs = transferPermissions(dbUser, permissions, to);
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD_ALL), transReqs, String.class, aopAuth.getHeaders());
	}
	
	@Override
	public void clone(U from, U to) throws Exception {
		U admin = getUserSrvc().authinticate();
		U dbUser = getUserSrvc().get(from.getEmail());
		Collection<ApplicationPermissionRequest<U>> permReqs = clonePermissions(admin, get(dbUser), to);
		Util.restPostCall(Util.getUri(URI.PERMISSION_ADD_ALL), permReqs, String.class, aopAuth.getHeaders());
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
		List<Permission> permissions = get(user);
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

	@SuppressWarnings("unchecked")
	@Override
	public List<Permission> get(UUserAbs user) {
		try {
			return Util.restGetCall(Util.getUri(URI.PERMISSION_ADD_ALL), List.class, aopAuth.getHeaders());
		} catch (Exception e) {
			return new ArrayList<Permission>();
		}
	}

	protected Collection<ApplicationPermissionRequest<U>> grantPermissions(U dbUser, Collection<Permission> perms, U to) {
		List<Permission> userPerms = get(dbUser);
		List<ApplicationPermissionRequest<U>> permReqs = new ArrayList<ApplicationPermissionRequest<U>>();
		for (Permission perm : perms) {
			boolean found = false;
			for (Permission uPerm : userPerms) {
				boolean eq = uPerm.equals(perm);
				boolean isAdminOoao = isType(uPerm, Permission.ADMIN, Permission.OAO);
				boolean isPsuedoOvalidation = isType(uPerm, Permission.PSUEDO, Permission.VALIDATION);
				if (eq && isAdminOoao) {
					found = true;

					perm.setGrantedFromUserId(dbUser.getId());
					ApplicationPermissionRequest<U> apq = createNewRequest(dbUser, to, perm, false);
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

	protected Collection<ApplicationPermissionRequest<U>> removePermissions(U dbUser, Collection<Permission> perms) {
		List<Permission> userPerms = get(dbUser);
		List<ApplicationPermissionRequest<U>> permReqs = new ArrayList<ApplicationPermissionRequest<U>>();
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
					
					ApplicationPermissionRequest<U> apq = new ApplicationPermissionRequest<U>();
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
	
	protected Collection<ApplicationPermissionRequest<U>> transferPermissions(U dbUser, Collection<Permission> perms, U to) {
		List<Permission> userPerms = get(dbUser);
		Collection<ApplicationPermissionRequest<U>> permReqs = new ArrayList<ApplicationPermissionRequest<U>>();
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
					
					ApplicationPermissionRequest<U> apq = createNewRequest(dbUser, to, uPerm, true);
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
	
	protected Collection<ApplicationPermissionRequest<U>> clonePermissions(U dbUser, Collection<Permission> perms, U to) {
		List<Permission> userPerms = get(dbUser);
		List<ApplicationPermissionRequest<U>> permReqs = new ArrayList<ApplicationPermissionRequest<U>>();
		for (Permission perm : perms) {
			boolean found = false;
			for (Permission uPerm : userPerms) {
				if (uPerm == null) {
					break;
				}

				if (uPerm.equals(perm)) {
					found = true;
					
					ApplicationPermissionRequest<U> apq = createNewRequest(dbUser, to, perm, false);
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
	
	protected ApplicationPermissionRequest<U> createNewRequest(U from, U to, Permission perm, boolean isTransfer) {
		perm.setUserId(to.getId());
		if (!isTransfer) {
			perm.setId(null);
			if (perm.getType().equals(Permission.OAO)) {
				perm.setType(Permission.PSUEDO);
			}
		}
		
		ApplicationPermissionRequest<U> apq = new ApplicationPermissionRequest<U>();
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
