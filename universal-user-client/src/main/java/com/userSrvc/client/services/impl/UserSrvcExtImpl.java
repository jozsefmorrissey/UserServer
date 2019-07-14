package com.userSrvc.client.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserUrl;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.repo.UserBaseRepository;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

@Service
public class UserSrvcExtImpl<U extends UUserAbs> implements UserSrvcExt<U> {

	@Autowired
	@Qualifier("UUser")
	U user;
	
	UserBaseRepository<U> localRepo;
	Class<U> clazz;
	
	public U loginUser(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_LOGIN), user, clazz);
		if (localRepo != null) {
			U localUser = localRepo.getByEmail(srvcUser.getEmail());
			localUser.merge(srvcUser);
			return localUser;
		}

		return srvcUser;
	}
	
	@PostConstruct
	public void init() throws ClassNotFoundException {
		String classStr = this.user.getClass().getCanonicalName().replaceAll("(.*?)\\$.*", "$1");
		clazz = (Class<U>) Class.forName(classStr);
		localRepo = getRepo();
	}

	@SuppressWarnings("unchecked")
	public U get(String emailOid) throws RestResponseException {
		UUserAbs uu = Util.restGetCall(Util.getUri("/user/" + emailOid), clazz);
		
		if (localRepo != null) {
			U localUser = localRepo.getOne(uu.getId());
			localUser.merge(uu);
			return localUser;
		}
		
		return (U) uu;
	}

	public U updateSrvc(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_UPDATE), user, clazz);
		if (localRepo != null) {
			U localUser = localRepo.getByEmail(srvcUser.getEmail());
			localUser.merge(srvcUser);
			return localRepo.saveAndFlush(localUser);
		}

		return srvcUser;
	}

	public U add(U user) throws RestResponseException {
		U srvcUser = Util.restPostCall(Util.getUri(URI.USER_ADD), user, clazz);
		if (localRepo != null) {
			user.merge(srvcUser);
			U localUser = localRepo.saveAndFlush(user);
			return localUser;
		}
		
		return srvcUser;
	}

	public U authinticateUser(U user) throws RestResponseException {
		return Util.restPostCall(Util.getUri(URI.USER_AUTH), user, clazz);
	}

	public void updatePassword(U user) throws RestResponseException {
		Util.restPostCall(Util.getUri(URI.USER_UPDATE_PASSWORD), user, String.class);
	}

	public void resetPassword(UserUrl<U> userUrl) throws RestResponseException {
		Util.restPostCall(Util.getUri(URI.USER_RESET_PASSWORD), userUrl, String.class);
	}


	public List<U> get(Collection<Long> ids) throws RestResponseException {
		List<Map> maps = Util.restPostCall(Util.getUri(URI.USER_ALL), ids, ArrayList.class);
		List<U> srvcUsers = Util.convertMapListToObjects(maps, clazz);
		
		if (localRepo != null) {
			List<U> localUsers = localRepo.findAllById(ids);
			UUserAbs.merg(localUsers, srvcUsers);
			return localUsers;
		}

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
}
