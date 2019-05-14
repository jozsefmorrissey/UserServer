package com.userSrvc.client;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.userSrvc.client.entities.GenUser;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.SrvcProps;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.services.impl.UserSrvcExtImpl;
import com.userSrvc.client.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { UserSrvcExtImpl.class, SrvcProps.class })
public class ClientTest
{
	@Autowired
	private UserSrvcExt userSrvcExt;

	String emailValid;
	String emailInvalid = "badFormat@email,com";
	String passwordValid = "garbally gook";
	String passwordUpdated = "You'll never get this";
	String passwordInvalid = "garbally gook2";
	String nameOriginal = "Murray Rothbard";
	String nameUpdated = "Freidric Engles";
	String validToken;
	String invalidToken = "shhhhhhh";
	long originalId;
	UUserAbs user = buildValidUser();

	private UUserAbs buildValidUser() {
		emailValid = Util.randomString(50, "[a-zA-Z0-9@\\.]", "[a-zA-Z0-9]{1,}@[a-zA-Z0-9]{1,}\\.[a-zA-Z0-9]{1,}");
		UUserAbs u = new GenUser(0, nameOriginal, emailValid, passwordValid);
		u.setUserToken(validToken);
		return u;
	}
	
	@Test
    public void testApp()
    {
    	add();
    	login();
    	authinticate();
    	update();
    	updatePass();
    }
    
    public void add() {
    	// Add valid credentails
    	try {
			userSrvcExt.add(user);
			assertTrue(true);
		} catch (RestResponseException e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
    	
    	// Add valid credentails with existing email
    	try {
    		userSrvcExt.add(user);
    		assertTrue(false);
    	} catch (RestResponseException e) {
    		assertTrue(Util.responseExceptContains(e, 
    				ERROR_MSGS.EMAIL_ALREADY_REGISTERED));
    	}

    	// Try to add credentails with invalid email;
    	user.setEmail(emailInvalid);
    	try {
    		userSrvcExt.add(user);
    		assertTrue(false);
    	} catch (RestResponseException e) {
    		assertTrue(Util.responseExceptContains(e, 
    				ERROR_MSGS.EMAIL_INVALID_FORMAT));
    	}
    	
    	user.setEmail(emailValid);
    }
    
    public void login() {
    	try {
			UUserAbs tokenCarrier = userSrvcExt.loginUser(user);
			this.validToken = tokenCarrier.getUserToken();
			this.originalId = tokenCarrier.getId();
			assertTrue(true);
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    	
    	user.setEmail(emailInvalid);
    	try {
			userSrvcExt.loginUser(user);
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.EMAIL_DOES_NOT_EXIST));
		}
    	user.setPassword(passwordInvalid);
    	user.setEmail(emailValid);
    	try {
			userSrvcExt.loginUser(user);
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INVALID_PASSWORD));
		}
    }
    
    public void authinticate() {
    	try {
			userSrvcExt.authinticateUser(user);
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.NO_TOKEN_PROVIDED));
		}
    	user.setUserToken(validToken);
    	try {
			userSrvcExt.authinticateUser(user);
			assertTrue(true);
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    	user.setUserToken(invalidToken);
    	try {
			userSrvcExt.authinticateUser(user);
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INCORRECT_CREDENTIALS));
		}
    }
    
    public void update() {
    	user.setName(nameUpdated);
    	user.setUserToken(validToken);
    	try {
			userSrvcExt.update(user);
			user = userSrvcExt.getUser(user);
			assertTrue(nameUpdated.equals(user.getName()));
			assertTrue(user.getId().equals(originalId));
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    }
    
    public void updatePass() {
    	try {
			user.setPassword(passwordUpdated);
	    	user.setUserToken(validToken);
			userSrvcExt.updatePassword(user);
			assertTrue(true);
			user = userSrvcExt.loginUser(user);
			assertTrue(user.getId() == this.originalId);
			user.setPassword(passwordValid);
			userSrvcExt.loginUser(user);
			assertTrue(false);
    	} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INVALID_PASSWORD));
		}
    	
    }
}
