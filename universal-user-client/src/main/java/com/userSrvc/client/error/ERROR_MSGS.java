package com.userSrvc.client.error;

public interface ERROR_MSGS {
	String EMAIL_ALREADY_REGISTERED = "Email address already registered.";
	String EMAIL_DOES_NOT_EXIST = "Email Does Not Exist";
	String EMAIL_NOT_REGISTERED = "Email not registered";
	String EMAIL_INVALID_FORMAT = "Email format invalid desired format: [email id]@[site id].[domain]";

	String INVALID_ID = "The id recieved is not valid";
	
	String INVALID_PASSWORD = "Invalid password";
	String NO_TOKEN_PROVIDED = "No user token provided";
	String INCORRECT_USER_CREDENTIALS = "Incorrect user credentials";
	String INCORRECT_APP_CREDENTIALS = "Incorrect app credentials";
	
	String USERNAME_NOT_DEFINED = "Username must be defined";
	
	String BYPASS_CREDENTIAL_ERROR = "This error can be bypassed by setting "
			+ "uu.failon.invalid.credentials to false. Only do so for debugging"
			+ "and testing purposes.";
	
	public default String multiMessage(String...msgs) {
		return String.join(": ", msgs);
	}
	
}
