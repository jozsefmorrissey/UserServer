package com.userSrvc.client.marker;

public interface HasType extends HasId {
	public String getObjectType();
	public boolean lockdown();
}
