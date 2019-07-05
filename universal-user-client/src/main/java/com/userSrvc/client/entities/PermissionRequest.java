package com.userSrvc.client.entities;

public class PermissionRequest {
	private UUserAbs to;
	private UUserAbs from;
	private Permission permission;
	
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	public UUserAbs getTo() {
		return to;
	}
	public void setTo(UUserAbs to) {
		this.to = to;
	}
	public UUserAbs getFrom() {
		return from;
	}
	public void setFrom(UUserAbs from) {
		this.from = from;
	}
}
