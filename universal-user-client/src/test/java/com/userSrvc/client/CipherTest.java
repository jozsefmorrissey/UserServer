package com.userSrvc.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.userSrvc.client.util.AES;
import com.userSrvc.client.util.Util;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {AES.class})
public class CipherTest {

	@Test
	public void test() {
		String ogString = Util.randomString(40, "[a-zA-Z0-9]", ".*");
		String msg = ogString;
		System.out.println(msg);
		msg = AES.encrypt(msg);
		assertFalse(msg.equals(ogString));
		System.out.println(msg);
		msg = AES.decrypt(msg);
		assertTrue(msg.equals(ogString));
	}
}
