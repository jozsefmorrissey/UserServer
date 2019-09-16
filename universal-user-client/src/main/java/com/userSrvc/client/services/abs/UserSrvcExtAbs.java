package com.userSrvc.client.services.abs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.repo.UserBaseRepository;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

public abstract class UserSrvcExtAbs<U extends UUserAbs> implements UserSrvcExt<U> {

	Random rand = new Random();
	
	@Autowired
	ApplicationContext appContext;
	
	U user;
	
	@Autowired
	AopAuth<U> aopAuth;
	
	public abstract U create(U user);
	
	UserBaseRepository<U> localRepo;
	Class<U> clazz;
	
	private void mergeUsers(U local, U remote) {
		long id = local.getId();
		local.merge(remote);
		local.setId(id);
	}
	
	private U updateLocal(U remote) {
		if (localRepo != null) {
			U local = localRepo.getByEmail(remote.getEmail());
			if (local == null) {
				local = localRepo.saveAndFlush(remote);
			}
			mergeUsers(local, remote);
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
		U srvcUser = Util.restGetCall(Util.getUri(URI.USER_LOGIN), clazz,
				getHeaders(aopAuth.getCurrentUser()));
		return updateLocal(srvcUser);
	}
	
	@PostConstruct
	public void init() throws ClassNotFoundException {
		user = (U) appContext.getBean("UUser");
		String classStr = this.user.getClass().getCanonicalName().replaceAll("(.*?)\\$.*", "$1");
		clazz = (Class<U>) Class.forName(classStr);
		localRepo = getRepo();
	}

	@SuppressWarnings("unchecked")
	public U get(String emailOid) throws RestResponseException {
		if (emailOid.matches("^[0-9]*$") && getRepo() != null) {
			return getRepo().getOne(Long.parseLong(emailOid));
		}
		U uu = Util.restGetCall(Util.getUri("/user/" + emailOid), clazz,
				getHeaders(aopAuth.getCurrentUser()));
		
		return updateLocal(uu);
	}

	public U updateSrvc(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_UPDATE), user, clazz,
				getHeaders(aopAuth.getCurrentUser()));

		return updateLocal(srvcUser);
	}

	public U add(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_ADD), user, clazz,
				getHeaders(aopAuth.getCurrentUser()));

		user.setId(srvcUser.getId());
		localRepo.saveAndFlush(user);
		
		user.merge(srvcUser);
		return updateLocal(user);
	}

	public U authinticate(U user) throws RestResponseException {
		U srvcUser = Util.restGetCall(Util.getUri(URI.USER_AUTH), clazz,
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


	public List<U> get(Collection<Long> ids) throws RestResponseException {
		List<Map> maps = Util.restPostCall(Util.getUri(URI.USER_ALL), ids, ArrayList.class,
				getHeaders(aopAuth.getCurrentUser()));
		List<U> srvcUsers = Util.convertMapListToObjects(maps, clazz);
		
		return mergeWithLocal(ids, srvcUsers);
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
	    if (user != null) {
		    httpHeaders.add(AopAuth.EMAIL, "" + user.getEmail());
		    httpHeaders.add(AopAuth.PASSWORD, user.getPassword());
		    httpHeaders.add(AopAuth.TOKEN, user.getToken());
		    aopAuth.getCurrentDebugGui().addHeader(httpHeaders);
//		    httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON.toString());
//		    httpHeaders.add("Accept", MediaType.APPLICATION_JSON.toString());
	    }
	    return httpHeaders;
	}
	
	private List<U> mergeWithLocal(Collection<Long> ids, List<U> srvcUsers) {
		if (localRepo == null) {
			return srvcUsers;
		}
		List<U> localUsers = localRepo.findAllById(ids);
		for (U srvcUser : srvcUsers) {
			boolean found = false;
			for (U localUser : localUsers) {
				if (srvcUser.getId().equals(localUser.getId())) {
					mergeUsers(localUser, srvcUser);
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
