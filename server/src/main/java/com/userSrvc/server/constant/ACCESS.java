package com.userSrvc.server.constant;

import com.userSrvc.client.util.Util;

public enum ACCESS {

	USER_SCOPE_PUBLIC("Public", 1),
	USER_SCOPE_PRIVATE("Private", 2),
	USER_SCOPE_ASSOSIATE("Associate", 3),
	
	USER_LIST_BLACK("BlackList", 4),
	USER_LIST_WHITE("WhiteList", 5),
	
	APP_LIST_INCLUSIVE("Inclusive", 6),
	APP_LIST_EXCLUSIVE("Exclusive", 7);
	
	private String name;
	private int id;
	
	ACCESS(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

	
	@Override
	public String toString() {
		return this.name;
	}
	
	public static void main(String...args) {
		System.out.println(Util.toJson(ACCESS.USER_SCOPE_ASSOSIATE));
	}
	
	public static ACCESS[] userScopes() {
		return new ACCESS[]{USER_SCOPE_PUBLIC, 
				USER_SCOPE_ASSOSIATE, 
				USER_SCOPE_PRIVATE};
	}

	public static ACCESS[] userExceptionLists() {
		return new ACCESS[]{USER_LIST_WHITE, 
				USER_LIST_BLACK};
	}
	
	public static ACCESS[] appLists() {
		return new ACCESS[]{APP_LIST_INCLUSIVE, 
				APP_LIST_INCLUSIVE};
	}
}
