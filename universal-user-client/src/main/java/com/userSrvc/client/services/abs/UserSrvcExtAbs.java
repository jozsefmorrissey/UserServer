package com.userSrvc.client.services.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.repo.UserBaseRepository;
import com.userSrvc.client.services.UserSrvc;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.services.impl.UserSrvcExtImpl;
import com.userSrvc.client.util.Util;

public abstract class UserSrvcExtAbs<U extends UUserAbs> implements UserSrvcExt<U> {

	@Autowired
	ApplicationContext appContext;
	
	U user;
	
	@Autowired
	AopAuth<U> aopAuth;
	
	UserBaseRepository<U> localRepo;
	Class<U> clazz;
	
	private U updateLocal(U remote) {
		if (localRepo != null) {
			U local = localRepo.getByEmail(remote.getEmail());
			try {
				local = localRepo.getOne(remote.getId());
				local.merge(remote);

				return localRepo.saveAndFlush(local);
			} catch (EntityNotFoundException e) {
				// TODO: change to a logger
				System.out.println("Warning no local data");
			}
		}
		return remote;
	}
	
	public U login() throws RestResponseException {
		U srvcUser = Util.restGetCall(Util.getUri(URI.USER_LOGIN), clazz,
				UserSrvc.getHeaders(aopAuth.getCurrentUser()));
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
		U uu = Util.restGetCall(Util.getUri("/user/" + emailOid), clazz,
				UserSrvc.getHeaders(aopAuth.getCurrentUser()));
		
		return updateLocal(uu);
	}

	public U updateSrvc(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_UPDATE), user, clazz,
				UserSrvc.getHeaders(aopAuth.getCurrentUser()));

		return updateLocal(srvcUser);
	}

	public U add(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_ADD), user, clazz,
				UserSrvc.getHeaders(aopAuth.getCurrentUser()));

		return updateLocal(srvcUser);
	}

	public U authinticate(U user) throws RestResponseException {
		return Util.restPostCall(Util.getUri(URI.USER_AUTH), user, clazz,
				UserSrvc.getHeaders(aopAuth.getCurrentUser()));
	}

	public U authinticate() throws RestResponseException {
		return authinticate(aopAuth.getCurrentUser());
	}

	public void updatePassword() throws RestResponseException {
		Util.restPostCall(Util.getUri(URI.USER_UPDATE_PASSWORD), user, String.class,
				UserSrvc.getHeaders(aopAuth.getCurrentUser()));
	}

	public void resetPassword(String url) throws RestResponseException {
		Util.restPostCall(Util.getUri(URI.USER_RESET_PASSWORD), url, 
				String.class, UserSrvc.getHeaders(aopAuth.getCurrentUser()));
	}


	public List<U> get(Collection<Long> ids) throws RestResponseException {
		List<Map> maps = Util.restPostCall(Util.getUri(URI.USER_ALL), ids, ArrayList.class,
				UserSrvc.getHeaders(aopAuth.getCurrentUser()));
		List<U> srvcUsers = Util.convertMapListToObjects(maps, clazz);
		
		return srvcUsers;
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
				String.class, UserSrvc.getHeaders(aopAuth.getCurrentUser()));
		U user = aopAuth.getCurrentUser();
		user.setEmail(newEmail);
		localRepo.saveAndFlush(user);
	}

	@Override
	public List<U> clean(List<U> users) {

		return null;
	}


}
