package com.userSrvc.client.services.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.ConnectionState;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.repo.UserBaseRepository;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.DebugGui;
import com.userSrvc.client.util.Util;

public abstract class UserSrvcExtAbs<U extends UUserAbs> implements UserSrvcExt<U> {

	@Autowired
	ApplicationContext appContext;
	
	U user;
	
	@Autowired
	AopAuth<U, ?> aopAuth;
	
	public abstract U create(U user);
	
	UserBaseRepository<U> localRepo;
	Class<U> clazz;
	
	private void mergeLocal(U local, U remote) {
		Long id = local.getId();
		local.merge(remote);
		local.setId(id);
	}

	private void mergeRemote(U remote, U local) {
		Long id = local.getId();
		remote.merge(local);
		remote.setId(id);
	}
	
	private U updateLocal(U remote) {
		return updateLocal(remote, false);
	}
	
	private U updateLocal(U remote, boolean isUpdate) {
		if (localRepo != null) {
			U local;
			if (isUpdate) {
				U loggedInUser = aopAuth.getCurrentUser();
				local = localRepo.getByEmail(loggedInUser.getEmail());
			} else {
				local = localRepo.getByEmail(remote.getEmail());
			}
			if (local == null) {
				local = localRepo.saveAndFlush(remote);
			}
			mergeLocal(local, remote);
			try {
				localRepo.saveAndFlush(local);
			} catch(Exception e2) {
				local = localRepo.saveAndFlush(create(remote));
			}
			return local;
		}
		return remote;
	}
	
	public U login() throws RestResponseException {
		return login(aopAuth.getCurrentUser());
	}

	public U login(U user) throws RestResponseException {
		U srvcUser = Util.restGetCall(Util.getUri(URI.USER_LOGIN), clazz,
				getHeaders(user));
		return updateLocal(srvcUser);
	}
	
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() throws ClassNotFoundException {
		user = (U) appContext.getBean("UUser");
		String classStr = this.user.getClass().getCanonicalName().replaceAll("(.*?)\\$.*", "$1");
		clazz = (Class<U>) Class.forName(classStr);
		localRepo = getRepo();
	}

	public U get(String emailOid) throws RestResponseException {
		if (emailOid.matches("^(-|)[0-9]*$") && getRepo() != null) {
			U localUser = getRepo().getOne(Long.parseLong(emailOid));
			if (localUser != null) {
				emailOid = localUser.getEmail();
			} else {
				return null;
			}
		}
		U uu = Util.restGetCall(Util.getUri("/user/" + emailOid), clazz,
				getHeaders(aopAuth.getCurrentUser()));
		
		return updateLocal(uu);
	}

	public U updateSrvc(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_UPDATE), user, clazz,
				getHeaders(aopAuth.getCurrentUser()));
		mergeRemote(srvcUser, user);
		return updateLocal(srvcUser, true);
	}

	public U add(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_ADD), user, clazz,
				getHeaders(aopAuth.getCurrentUser()));

		mergeLocal(user, srvcUser);
		return localRepo.saveAndFlush(user);
	}

	public U authinticate(U user) throws RestResponseException {
		U srvcUser = Util.restGetCall(Util.getUri(URI.USER_AUTHINTICATE), clazz,
					getHeaders(aopAuth.getCurrentUser()));
		return updateLocal(srvcUser);
	}

	public U authinticate() throws RestResponseException {
		return authinticate(aopAuth.getCurrentUser());
	}

	public void updatePassword() throws RestResponseException {
		Util.restGetCall(Util.getUri(URI.USER_UPDATE_PASSWORD), String.class,
				getHeaders(aopAuth.getCurrentUser()));
	}

	public void resetPassword(String url) throws RestResponseException {
		Util.restPostCall(Util.getUri(URI.USER_RESET_PASSWORD), url, 
				String.class, getHeaders(aopAuth.getCurrentUser()));
	}


	public List<U> get(Collection<String> emails) throws RestResponseException {
		List<Map<?,?>> maps = Util.restPostCall(Util.getUri(URI.USER_ALL), emails, ArrayList.class,
				getHeaders(aopAuth.getCurrentUser()));
		List<U> srvcUsers = Util.convertMapListToObjects(maps, clazz);
		
		
		return mergeWithLocal(emails, srvcUsers);
	}

	public U get(long id) throws Exception {
		U srvcUser = get(new Long(id).toString());
		return srvcUser;
	}

	@Override
	public U update(U user) throws RestResponseException {
		if (localRepo != null) {
			return localRepo.saveAndFlush(user);
		}
		return user;
	}

	@Override
	public UserBaseRepository<U> getRepo() {
		return null;
	}


	@Override
	public void updateEmail(String newEmail) throws Exception {
		Util.restPostCall(Util.getUri(URI.USER_UPDATE_EMAIL), newEmail, 
				String.class, getHeaders(aopAuth.getCurrentUser()));
		U user = aopAuth.getCurrentUser();
		user.setEmail(newEmail);
		localRepo.saveAndFlush(user);
	}

	@Override
	public List<U> clean(List<U> users) {

		return null;
	}

	public HttpHeaders getHeaders(U user) {
		HttpHeaders httpHeaders = new HttpHeaders();
	    httpHeaders.add(ConnectionState.DEVICE_IDENTIFIER, aopAuth.getDeviceIdentifier());
	    if (user != null) {
		    httpHeaders.add(ConnectionState.EMAIL, "" + user.getEmail());
		    httpHeaders.add(ConnectionState.PASSWORD, user.getPassword());
		    httpHeaders.add(ConnectionState.TOKEN, user.getToken().getToken());
		    httpHeaders.add(ConnectionState.DEVICE_IDENTIFIER, user.getToken().getDeviceIdentifier());
		    DebugGui.addHeader(httpHeaders);
//		    httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON.toString());
//		    httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
	    }
	    return httpHeaders;
	}
	
	private List<U> mergeWithLocal(Collection<String> emails, List<U> srvcUsers) {
		if (localRepo == null) {
			return srvcUsers;
		}
		List<U> localUsers = localRepo.findByEmailIn(emails);
		for (U srvcUser : srvcUsers) {
			boolean found = false;
			for (U localUser : localUsers) {
				if (srvcUser.getEmail().equals(localUser.getEmail())) {
					mergeLocal(localUser, srvcUser);
					found = true;
				}
			}
			if (!found) {
				localUsers.add(updateLocal(srvcUser));
			}
		}
		return localUsers;
	}

}
