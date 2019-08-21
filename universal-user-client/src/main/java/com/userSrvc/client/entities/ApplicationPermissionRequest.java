package com.userSrvc.client.entities;

public class ApplicationPermissionRequest <U extends UUserAbs> {
	private U application;
	private Permission permission;
	private Permission parent;
	
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	public Permission getParent() {
		return parent;
	}
	public void setParent(Permission parent) {
		this.parent = parent;
	}
	public U getApplication() {
		return application;
	}
	public void setApplication(U application) {
		this.application = application;
	}
}
