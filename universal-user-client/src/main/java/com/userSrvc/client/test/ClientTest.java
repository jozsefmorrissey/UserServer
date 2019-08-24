package com.userSrvc.client.test;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Test;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.util.Util;

import lombok.Data;

@Data
public abstract class ClientTest
{
	private String emailValid = Util.randomString(50, "[a-zA-Z0-9@\\.]", "[a-zA-Z0-9]{1,}@[a-zA-Z0-9]{1,}\\.[a-zA-Z0-9]{1,}");
	private String emailInvalid = "badFormat@email,com";
	private String passwordValid = "garbally gook";
	private String passwordUpdated = "You'll never get this";
	private String passwordInvalid = "garbally gook2";
	private String nameOriginal = "Murray Rothbard";
	private String nameUpdated = "Freidric Engles";
	private HashMap<String, String> validTokens = new HashMap<String, String>();
	private String invalidToken = "shhhhhhh";
	private long originalId;
	private UUserAbs user = buildValidUser();

	protected abstract UUserAbs buildValidUser();
	protected abstract void post();
    public abstract UserSrvcExt<UUserAbs> getUserSrvcExt();

    
	@Test
    public void testApp() throws Exception
    {
    	add();
    	login();
    	authinticate();
    	update();
    	updatePass();
    	post();
    }
    
    public void add() throws Exception {
    	// Add valid credentials
    	try {
			getUserSrvcExt().add(user);
			assertTrue(true);
		} catch (RestResponseException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
    	
    	// Add valid credentials with existing email
    	try {
    		getUserSrvcExt().add(user);
    		assertTrue(false);
    	} catch (RestResponseException e) {
    		assertTrue(Util.responseExceptContains(e, 
    				ERROR_MSGS.EMAIL_ALREADY_REGISTERED));
    	}

    	// Try to add credentials with invalid email;
    	user.setEmail(emailInvalid);
    	try {
    		getUserSrvcExt().add(user);
    		assertTrue(false);
    	} catch (RestResponseException e) {
    		assertTrue(Util.responseExceptContains(e, 
    				ERROR_MSGS.EMAIL_INVALID_FORMAT));
    	}
    	
    	user.setEmail(emailValid);
    }
    
	public void login() throws Exception {
    	try {
			UUserAbs tokenCarrier = getUserSrvcExt().login();
			saveValidToken(tokenCarrier);
			this.originalId = tokenCarrier.getId();
			assertTrue(true);
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    	
    	user.setEmail(emailInvalid);
    	try {
			getUserSrvcExt().login();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.EMAIL_DOES_NOT_EXIST));
		}
    	user.setPassword(passwordInvalid);
    	user.setEmail(emailValid);
    	try {
			getUserSrvcExt().login();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INVALID_PASSWORD));
		}
    }
    
    public void authinticate() throws Exception {
    	try {
    		user.setPassword(null);
			getUserSrvcExt().authinticate();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.NO_TOKEN_PROVIDED));
		}
    	setValidToken(user);
    	try {
			getUserSrvcExt().authinticate();
			assertTrue(true);
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    	user.setToken(invalidToken);
    	try {
			getUserSrvcExt().authinticate();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INCORRECT_CREDENTIALS));
		}
    }
    
    public void update() throws Exception {
    	user.setFullName(nameUpdated);
    	setValidToken(user);
    	try {
			getUserSrvcExt().update(user);
			user = getUserSrvcExt().get(user.getEmail());
			assertTrue(nameUpdated.equals(user.getFullName()));
			assertTrue(user.getId() == originalId);
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    }
    
    public void updatePass() throws Exception {
    	try {
			user.setPassword(passwordUpdated);
	    	setValidToken(user);
			getUserSrvcExt().updatePassword();
			assertTrue(true);
			user = getUserSrvcExt().login();
			assertTrue(user.getId() == this.originalId);
			user.setPassword(passwordValid);
			getUserSrvcExt().login();
			assertTrue(false);
    	} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INVALID_PASSWORD));
		}
    }
    
    public String getValidToken(UUserAbs user) {
    	return this.validTokens.get(user.getEmail());
    }
    
    public void saveValidToken(UUserAbs user) {
    	this.validTokens.put(user.getEmail(), user.getToken());
    }
    
    public void setValidToken(UUserAbs user) {
    	user.setToken(getValidToken(user));
    }
}
