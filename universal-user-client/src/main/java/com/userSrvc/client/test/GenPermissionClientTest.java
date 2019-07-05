package com.userSrvc.client.test;

import static org.junit.Assert.fail;

import javax.annotation.PostConstruct;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.error.RestResponseException;
import com.userSrvc.client.services.SrvcProps;
import com.userSrvc.client.services.impl.GenPermissionSrvcExtImpl;
import com.userSrvc.client.services.impl.UserSrvcExtImpl;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { UserSrvcExtImpl.class, SrvcProps.class, 
		GenClientTest.class, GenPermissionSrvcExtImpl.class })
public class GenPermissionClientTest extends PermissionClientTest {

	private UUserAbs appUser;
	
	@PostConstruct
	public void init() {
		super.init();
		appUser = this.getAppUser();
		((GenPermissionSrvcExtImpl)this.getPermSrvc()).setApplicationUser(appUser);
	}
	
	@Override
	protected void post() {
		// TODO Auto-generated method stub
		
	}
}
