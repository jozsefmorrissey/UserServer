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
import com.userSrvc.client.services.abs.UserSrvcExtAbs;
import com.userSrvc.client.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { UserSrvcExtAbs.class, SrvcProps.class })
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
//		u.setToken(validToken);
		return u;
	}
	
	@Test
    public void testApp() throws Exception
    {
//    	add();
//    	login();
    	authinticate();
//    	update();
//    	updatePass();
    }
    
    public void add() throws Exception {
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
    
    public void login() throws Exception {
    	try {
			UUserAbs tokenCarrier = userSrvcExt.login();
			this.validToken = tokenCarrier.getToken().getToken();
			this.originalId = tokenCarrier.getId();
			assertTrue(true);
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    	
    	user.setEmail(emailInvalid);
    	try {
			userSrvcExt.login();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.EMAIL_DOES_NOT_EXIST));
		}
    	user.setPassword(passwordInvalid);
    	user.setEmail(emailValid);
    	try {
			userSrvcExt.login();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INVALID_PASSWORD));
		}
    }
    
    public void authinticate() throws Exception {
    	try {
			userSrvcExt.authinticate();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.NO_TOKEN_PROVIDED));
		}
//    	user.setToken(validToken);
    	try {
			userSrvcExt.authinticate();
			assertTrue(true);
		} catch (RestResponseException e) {
			assertTrue(false);
		}
//    	user.setToken(invalidToken);
    	try {
			userSrvcExt.authinticate();
			assertTrue(false);
		} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INCORRECT_APP_CREDENTIALS));
		}
    }
    
    public void update() throws Exception {
    	user.setFullname(nameUpdated);
//    	user.setToken(validToken);
    	try {
			userSrvcExt.update(user);
			user = userSrvcExt.get(user.getEmail());
			assertTrue(nameUpdated.equals(user.getFullname()));
			assertTrue(user.getId().equals(originalId));
		} catch (RestResponseException e) {
			assertTrue(false);
		}
    }
    
    public void updatePass() throws Exception {
    	try {
			user.setPassword(passwordUpdated);
//	    	user.setToken(validToken);
			userSrvcExt.updatePassword();
			assertTrue(true);
			user = userSrvcExt.login();
			assertTrue(user.getId() == this.originalId);
			user.setPassword(passwordValid);
			userSrvcExt.login();
			assertTrue(false);
    	} catch (RestResponseException e) {
			assertTrue(Util.responseExceptContains(e, 
					ERROR_MSGS.INVALID_PASSWORD));
		}
    	
    }
}
