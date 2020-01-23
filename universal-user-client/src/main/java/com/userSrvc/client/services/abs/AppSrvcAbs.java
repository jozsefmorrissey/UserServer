package com.userSrvc.client.services.abs;

import java.rmi.AccessException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.AppAccessToken;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.repo.AppBaseRepository;
import com.userSrvc.client.repo.TokenRepository;
import com.userSrvc.client.services.AppSrvc;

public abstract class AppSrvcAbs <A extends App, T extends TokenRepository<AppAccessToken>> implements AppSrvc<A> {

	@Autowired
	AopAuth<?, A> aopAuth;
	
	@Autowired
	T tokenRepo;
	
	@Autowired
	AppBaseRepository<A> appRepo;
	
	@Override
	public A get(Long id) throws Exception {
		return appRepo.getOne(id);
	}

	@Override
	public A update(A app) throws Exception {
		return appRepo.saveAndFlush(app);
	}

	@Override
	public A authinticate() throws Exception {
		return authinticate(aopAuth.getCurrentApp());
	}

	@Override
	public A authinticate(A app) throws Exception {
		if (app == null) {
			return authinticate(-0l, null);
		}
		return authinticate(app.getId(), app.getAccessKey());
	}

	@Override
	public A authinticate(Long appId, String accessKey) throws Exception {
		A dbApp = get(appId);
		if (accessKey == null ||
				!accessKey.equals(dbApp.getAccessKey())) {
			throw new AccessException(ERROR_MSGS.INCORRECT_APP_CREDENTIALS);
		}
		AppAccessToken accessToken = new AppAccessToken(appId, aopAuth.getDeviceIdentifier());
		tokenRepo.saveAndFlush(accessToken);
		dbApp.setToken(accessToken);
		return dbApp;
	}

	@Override
	public HttpHeaders getHeaders(A app) {
		return aopAuth.getHeaders();
	}

	@Override
	public List<A> get(Collection<Long> ids) throws Exception {
		return appRepo.findAllById(ids);
	}

}
