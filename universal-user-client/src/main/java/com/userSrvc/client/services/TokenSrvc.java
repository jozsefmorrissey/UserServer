package com.userSrvc.client.services;

import java.nio.file.AccessDeniedException;

import com.userSrvc.client.entities.AccessToken;
import com.userSrvc.client.marker.HasToken;

public interface TokenSrvc {
	public <T extends AccessToken> void generateToken(HasToken<T> hasToken) throws AccessDeniedException;
	public <T extends AccessToken> void validateToken(HasToken<T> hasToken) throws AccessDeniedException;
}
