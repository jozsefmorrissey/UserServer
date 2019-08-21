package com.userSrvc.client.test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.userSrvc.client.entities.GenUser;
import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.services.SrvcProps;
import com.userSrvc.client.services.UserSrvcExt;
import com.userSrvc.client.services.abs.UserSrvcExtAbs;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { UserSrvcExtAbs.class, SrvcProps.class })
public class GenClientTest extends ClientTest {
	@Autowired
	private UserSrvcExt userSrvcExt;
	
	@Override
	protected UUserAbs buildValidUser() {
	UUserAbs u = new GenUser(0, this.getNameOriginal(), this.getEmailValid(), this.getPasswordValid());
	setValidToken(u);
		return u;
	}

	@Override
	public UserSrvcExt getUserSrvcExt() {
		return userSrvcExt;
	}

	@Override
	protected void post() {
		// TODO Auto-generated method stub
		
	}

}
