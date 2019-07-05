package com.userSrvc.client.constant;

public class URI extends ToJson{
	public static final String ALL = "/all";
	public static final String ID = "/{id}";
	public static final String UPDATE = "/update";
	public static final String EMAILoID = "/{emailOid}";
	
	public static final String USER = "/user";
	public static final String USER_ADD = USER + "/add";
	public static final String USER_LOGIN = USER + "/login";
	public static final String USER_EMAIL_O_ID = USER + EMAILoID;
	public static final String USER_ALL = USER + ALL;
	public static final String USER_UPDATE = USER + UPDATE;
	public static final String USER_AUTH = USER + "/authinticate";
	public static final String USER_UPDATE_PASSWORD = USER + UPDATE + "/password";
	public static final String USER_RESET_PASSWORD = USER + "/reset/password";

	public static final String USER_PHOTO_USER = USER + "/photo/{id}.";
	public static final String USER_PHOTO_URIS_USER_APP = USER + "/photo/uris/{userId}/{appId}";
	
	public static final String PERMISSION = "/permission";
	public static final String PERMISSION_ADD = PERMISSION + "/add";;
	public static final String PERMISSION_ADD_ALL = PERMISSION_ADD + ALL;
	public static final String PERMISSION_REMOVE = PERMISSION + "/remove";
	public static final String PERMISSION_REMOVE_ALL = PERMISSION_REMOVE + ALL;

	@Override
	public boolean addField(String name) {
		// TODO Auto-generated method stub
		return name.matches(".*_.*");
	}	
}
