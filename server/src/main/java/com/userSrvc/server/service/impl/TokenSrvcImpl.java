package com.userSrvc.server.service.impl;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.AccessToken;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.AppAccessToken;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.entities.UserAccessToken;
import com.userSrvc.client.marker.HasToken;
import com.userSrvc.client.repo.TokenRepository;
import com.userSrvc.client.services.TokenSrvc;
import com.userSrvc.server.entities.UUser;
import com.userSrvc.server.repo.AppAccessTokenRepo;
import com.userSrvc.server.repo.UserAccessTokenRepo;

@Service
public class TokenSrvcImpl implements TokenSrvc {
	@Value("${user.token.expiration.hours:24}")
	private Integer tokenExpirationLimitHours;
	
	@Autowired
	AopAuth<UUserAbs, ?> aopAuth;
	
	@Autowired
	UserAccessTokenRepo userTokenRepo;
	
	@Autowired
	AppAccessTokenRepo appTokenRepo;
	
	Map<Class<? extends AccessToken>, TokenRepository<?>> repoMap = 
			new HashMap<Class<? extends AccessToken>, TokenRepository<?>>();

	@PostConstruct
	private void init() {
		repoMap.put(UserAccessToken.class, userTokenRepo);
		repoMap.put(AppAccessToken.class, appTokenRepo);
	}
	
	@SuppressWarnings("unchecked")
	private <T extends AccessToken> TokenRepository<T> getRepo(HasToken<T> hasToken) {
		for (Class<? extends AccessToken> clazz : repoMap.keySet()) {
			if (clazz.equals(hasToken.emptyToken().getClass())) {
				return (TokenRepository<T>) repoMap.get(clazz);
			}
		}
		throw new AccessTokenConfigurationError(hasToken.getToken());
	}
	
	private  <T extends AccessToken> T getToken(HasToken<T> hasToken) throws AccessDeniedException {
		return getRepo(hasToken).getOne(hasToken.getId());
	}
	
	public static void main(String...args) throws AccessDeniedException {
		UserAccessToken ust = new UUser().emptyToken();
		AppAccessToken ast = new App().emptyToken();
		System.out.println(ust.getClass());
		System.out.println(ast.getClass());
	}

	@Override
	public <T extends AccessToken> void generateToken(HasToken<T> hasToken) throws AccessDeniedException {
		T token;
		token = hasToken.emptyToken();
		token.setDeviceIdentifier(aopAuth.getDeviceIdentifier());
		token.setObjectId(hasToken.getId());
		
		Long expiration = System.currentTimeMillis() + (tokenExpirationLimitHours * 3600);
		token.setExpiration(expiration);

		getRepo(hasToken).saveAndFlush(token);
		hasToken.setToken(token);
	}

	@Override
	public <T extends AccessToken> void validateToken(HasToken<T> hasToken) throws AccessDeniedException {
		T token = getToken(hasToken);
		if (token == null || !token.equals(hasToken.getToken())) {
			throw new AccessDeniedException("Invalid token");
		}
		if (token.getExpiration() < System.currentTimeMillis() * 0.80) {
			generateToken(hasToken);
		}
	}
	
	private static class AccessTokenConfigurationError extends Error {
		private static final long serialVersionUID = -1224827270455600551L;
		private Class<? extends AccessToken> tokenClass; 
		
		public AccessTokenConfigurationError(AccessToken accessToken) {
			this.tokenClass = accessToken.getClass();
		}
		@Override
		public String getMessage() {
			return "Token repository not registered: TokenSrviceImpl must be configured for AccessToken class '" + tokenClass + "'";
		}
		
	}
}
