package com.userSrvc.server;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.nio.file.AccessDeniedException;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.client.util.Util;
import com.userSrvc.server.controller.UserCtrl;

public class ClientTest extends Config
{
	@Autowired
	UserCtrl userCtrl;
	
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
		UUserAbs u = null;// = new GenUser(0, nameOriginal, emailValid, passwordValid);
//		u.setToken(validToken);
		return u;
	}
	
	@Test
    public void testApp() throws Exception
    {
//		AopAuthMock.setCurrentUser(user);
		System.out.println("P:" + user.getPassword());
    	add();
    	login();
    	authinticate();
    	update();
    	updatePass();
    }
    
    public void add() throws Exception {
    	// Add valid credentails
    	try {
			userCtrl.add(user);
			System.out.println("Password: " + user.getPassword());
			assertTrue(true);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
    	
    	// Add valid credentails with existing email
    	try {
    		userCtrl.add(user);
    		assertTrue(false);
    	} catch (ConstraintViolationException e) {
    		assertTrue(true);
    	}

    	// Try to add credentails with invalid email;
    	user.setEmail(emailInvalid);
    	try {
    		userCtrl.add(user);
    		assertTrue(false);
    	} catch (PropertyValueException e) {
    		assertTrue(true);
    	}
    	
    	user.setEmail(emailValid);
    }
    
    public void login() throws Exception {
    	try {
    		user.setPassword(passwordValid);
			System.out.println("Password: " + user.getPassword());
			UUserAbs tokenCarrier = userCtrl.login();
			this.validToken = tokenCarrier.getToken().getToken();
			this.originalId = tokenCarrier.getId();
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
    	
    	user.setEmail(emailInvalid);
    	try {
    		user.setPassword(passwordValid);
    		userCtrl.login();
			assertTrue(false);
		} catch (DataException e) {
			assert(e.getMessage().equals(ERROR_MSGS.EMAIL_DOES_NOT_EXIST));
		}
    	
    	user.setPassword(passwordInvalid);
    	user.setEmail(emailValid);
    	try {
    		userCtrl.login();
			assertTrue(false);
		} catch (PropertyValueException e) {
			assert(e.getMessage().contains(ERROR_MSGS.INVALID_PASSWORD));
		}
    }
    
    public void authinticate() throws Exception {
    	try {
    		user.setToken(null);
    		userCtrl.authinticate();
			fail();
		} catch (AccessDeniedException e) {
			assertTrue(e.getMessage().equals(ERROR_MSGS.NO_TOKEN_PROVIDED));
		}
//    	user.setToken(validToken);
    	try {
    		userCtrl.authinticate();
			assertTrue(true);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
//    	user.setToken(invalidToken);
    	try {
    		userCtrl.authinticate();
			assertTrue(false);
		} catch (AccessDeniedException e) {
			assertTrue(e.getMessage().equals(ERROR_MSGS.INCORRECT_APP_CREDENTIALS));
		}
    }
    
    public void update() throws Exception {
    	user.setFullname(nameUpdated);
//    	user.setToken(validToken);
    	try {
    		userCtrl.update(user);
			user = userCtrl.get(user.getEmail());
			assertTrue(nameUpdated.equals(user.getFullname()));
			assertTrue(user.getId().equals(originalId));
		} catch (Exception e) {
			fail();
		}

    	// TODO: Fix Aop so this can be tested.
//    	user.setUserToken(invalidToken);
//    	try {
//    		user.setFullName("This Should Not Save");
//    		userCtrl.update(user);
//    		fail();
//    	} catch (Exception e) {
//    		assertTrue(e.getMessage().contains(ERROR_MSGS.INCORRECT_CREDENTIALS));
//		}
    }
    
    public void updatePass() throws Exception {
    	try {
			user.setPassword(passwordUpdated);
//	    	user.setToken(validToken);
	    	userCtrl.updatePassword();
			assertTrue(true);
			user = userCtrl.login();
			assertTrue(user.getId() == this.originalId);
			user.setPassword(passwordValid);
			userCtrl.login();
			assertTrue(false);
    	} catch (PropertyValueException e) {
			assertTrue(e.getMessage().contains(ERROR_MSGS.INVALID_PASSWORD));
		}
    	
    }
}
