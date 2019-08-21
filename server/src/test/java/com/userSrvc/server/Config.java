package com.userSrvc.server;

import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

import com.userSrvc.client.aop.AopAuthMock;
import com.userSrvc.client.entities.GenUser;
import com.userSrvc.client.services.SrvcProps;
import com.userSrvc.client.services.impl.UserSrvcExtImpl;
import com.userSrvc.client.util.AES;
import com.userSrvc.server.controller.UserCtrl;
import com.userSrvc.server.service.impl.PermissionSrvcImpl;
import com.userSrvc.server.service.impl.UserPhotoSrvcImpl;
import com.userSrvc.server.service.impl.UserSrvcImpl;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@ContextConfiguration(classes = { UserCtrl.class, SrvcProps.class, AopAuthMock.class, GenUser.class, 
		UserSrvcImpl.class, UserPhotoSrvcImpl.class, PermissionSrvcImpl.class, AES.class, UserSrvcExtImpl.class })
@EntityScan("com.userSrvc")
@EnableJpaRepositories("com.userSrvc.server.repo")
public class Config  extends AbstractJUnit4SpringContextTests {

}
