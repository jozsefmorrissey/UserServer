package com.userSrvc.client.marker;

import com.userSrvc.client.entities.AccessToken;

public interface HasToken <T extends AccessToken> extends HasId {
	public T getToken();
	public T emptyToken();
	public void setToken(T token);
}
