package com.userSrvc.client.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.DatabaseIntegrityException;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.marker.HasType;
import com.userSrvc.client.services.PermissionSrvcExt;
import com.userSrvc.client.util.Util;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public abstract class PermissionClientTest
{
	@Autowired
	private ClientTest clientTest;
	
	@Autowired
	private PermissionSrvcExt<UUserAbs> permSrvc;
	
	private UUserAbs appUser;
	private UUserAbs u0;
	private UUserAbs u1;
	private UUserAbs u2;
	
	private String permType1 = "DummyValue1";
	private String permType2 = "DummyValue2";
	
	private Permission p1;
	private Permission p2;
	private Permission p3;
	private Permission p4;
	
	
	private List<Permission> pl1 = new ArrayList<Permission>();
	private List<Permission> pl2 = new ArrayList<Permission>();

	private String emailReg = "[a-zA-Z0-9]{1,}@[a-zA-Z0-9]{1,}\\.[a-zA-Z0-9]{1,}";
	private String emailChars = "[a-zA-Z0-9@\\.]";
	
	protected abstract void post();
	
	public UUserAbs getAppUser() {
		return appUser;
	}
    
	@PostConstruct
    public void init() {

		appUser = clientTest.buildValidUser();
		appUser.setEmail(Util.randomString(50, emailChars, emailReg));

		u0 = clientTest.buildValidUser();
		u0.setEmail(Util.randomString(50, emailChars, emailReg));

		u1 = clientTest.buildValidUser();
		u1.setEmail(Util.randomString(50, emailChars, emailReg));

		u2 = clientTest.buildValidUser();
		u2.setEmail(Util.randomString(50, emailChars, emailReg));
    	
    	p1 = buildPermission(u1, permType1, 3l);
    	p2 = buildPermission(u1, permType1, 3l);
    	p3 = buildPermission(u2, permType2, 3l);
    	p4 = buildPermission(u2, permType2, 3l);

    	
    	pl1.add(p1);
    	pl1.add(p2);
    	pl2.add(p3);
    	pl2.add(p4);
		//u1.setPermissions(pl1);
		try {
			clientTest.getUserSrvcExt().add(appUser);
			clientTest.getUserSrvcExt().add(u0);
			clientTest.getUserSrvcExt().add(u1);
			clientTest.getUserSrvcExt().add(u2);
			
			appUser = this.getClientTest().getUserSrvcExt().login();
			u0 = this.getClientTest().getUserSrvcExt().login();
			u1 = this.getClientTest().getUserSrvcExt().login();
			u2 = this.getClientTest().getUserSrvcExt().login();
		} catch (Exception e) {
			fail("Unable to add Users");
		}
    }
    	
	@Test
    @Rollback(true)
	public void testApp() throws Exception
    {
		verifyNoCascade();
		addAll();
		grantAll();
		transferAll();
		cloneAll();
		removeAll();
		System.out.println(clientTest.buildValidUser());
    	post();
    }
	
	public void verifyNoCascade() {
		try {
			UUserAbs u1FromDb = clientTest.getUserSrvcExt().get(u1.getEmail());
			assertTrue(u1FromDb.getPermissionTypes().size() == 0);
		} catch (Exception e) {
			fail("Failed to retrieve u1 from db");
		}
	}
    
    public void addAll() throws Exception {
    	try {
			permSrvc.add(u0, new Type1(1l), null);
	    	permSrvc.add(u0, new Type1(2l), null);
	    	permSrvc.add(u0, new Type1(3l), null);
	    	
	    	Permission parent = new Permission(null, null, null, permType1, 1l, null, null, null);
	    	
	    	permSrvc.add(u1, new Type2(1l), permSrvc.build(new Type1(1l)));
	    	
	    	permSrvc.add(u1, new Type2(2l), permSrvc.build(new Type1(2l)));
	    	permSrvc.add(u1, new Type2(3l), permSrvc.build(new Type1(3l)));
	    	
	    	UUserAbs u0db = this.clientTest.getUserSrvcExt().get(u0.getEmail());
	    	assertTrue(u0db.getPermissionTypes().size() == 6);
	    	
	    	UUserAbs u1db = this.clientTest.getUserSrvcExt().get(u1.getEmail());
	    	assertTrue(u1db.getPermissionTypes().size() == 3);
		} catch (DatabaseIntegrityException | RestResponseException e) {
			fail("Failed to add permissions to initial user");
		}
    }
    
    public void transferAll() {
    	try {
			UUserAbs u0db = this.clientTest.getUserSrvcExt().get(u0.getEmail());

        	List<Permission> transfers = null;// = u0db.getPermissionTypes().subList(2, 4);
			permSrvc.transfer(u0, u2, transfers);

			u0db = this.clientTest.getUserSrvcExt().get(u0.getEmail());
			assertTrue(u0db.getPermissionTypes().size() == 4);

			UUserAbs u2db = this.clientTest.getUserSrvcExt().get(u2.getEmail());
			assertTrue(u2db.getPermissionTypes().size() == 5);
			
			permSrvc.transfer(u2, u0, transfers);

			u0db = this.clientTest.getUserSrvcExt().get(u0.getEmail());
			assertTrue(u0db.getPermissionTypes().size() == 6);

		} catch (Exception e) {
			fail("Failed to transfer permissions");
		}
    }
    
    public void grantAll() {
    	try {
			UUserAbs u1db = this.clientTest.getUserSrvcExt().get(u1.getEmail());
			List<Permission> grants = null;// = u1db.getPermissionTypes();
			grants.get(0).setType(Permission.PSUEDO);
			grants.get(1).setType(Permission.VALIDATION);
			grants.get(2).setType(Permission.NO);

			permSrvc.grant(u2, grants);
			
			UUserAbs u2db = this.clientTest.getUserSrvcExt().get(u2.getEmail());
			assertTrue(u2db.getPermissionTypes().size() == 3);
			
    	} catch (Exception e) {
			fail("Failed to grant permissions");
		}

    }
    
    public void cloneAll() {
		try {
			UUserAbs u2db = this.clientTest.getUserSrvcExt().get(u2.getEmail());
			List<Permission> clones = null;// = u2db.getPermissionTypes();
			
			permSrvc.clone(u2, u1);
			
			UUserAbs u1db = this.clientTest.getUserSrvcExt().get(u1.getEmail());
			assertTrue(u1db.getPermissionTypes().size() == 6);
		} catch (Exception e) {
			fail("Failed to clone permissions");
		}
    }
    
    public void removeAll() {
		try {
			UUserAbs u2db = this.clientTest.getUserSrvcExt().get(u2.getEmail());
			List<String> removes = u2db.getPermissionTypes();
			UUserAbs u0db = this.clientTest.getUserSrvcExt().get(u0.getEmail());

			
//			permSrvc.remove(u0, u2, removes);
			
			u2db = this.clientTest.getUserSrvcExt().get(u2.getEmail());
			assertTrue(u2db.getPermissionTypes().size() == 0);
		} catch (Exception e) {
			fail("Failed to clone permissions");
		}
    }
    
    private Permission buildPermission(UUserAbs user, String refType, Long refId) {
    	return new Permission(null, 
    			appUser.getId(), 
    			user.getId(), 
    			refType, 
    			refId, 
    			null, 
    			null, 
    			null);
    }
    
    @AllArgsConstructor
    private class Type1 implements HasType {
    	private Long id;
		public Long getId() {return this.id;}
		public String getObjectType() {return "type1";}
		public boolean lockdown() {
			return false;
		}
    }

    @AllArgsConstructor
    private class Type2 implements HasType {
    	private Long id;
		public Long getId() {return this.id;}
		public String getObjectType() {return "type2";}
		public boolean lockdown() {
			return false;
		}
    }
}
