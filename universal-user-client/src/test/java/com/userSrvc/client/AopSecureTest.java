package com.userSrvc.client;

public class AopSecureTest {

//	@Test
//	public void securePrivateFields() {
//		UUserAbs user = new UUserAbs();
//		user.setPrivateFields("imageUrls");
//		user.setImageUrls(Arrays.asList(new String[] {"Dummy.com"}));
////		user.clean();
//		assertTrue(user.getImageUrls() == null);
//	}
//
//	@Test
//	public void secureProtectedFields() {
//		UUserAbs user = new UUserAbs();
//		user.setProtectedFields("imageUrls");
//		user.setImageUrls(Arrays.asList(new String[] {"Dummy.com"}));
////		user.clean();
//		assertTrue(user.getImageUrls() == null);
//	}
//
//	@Test
//	public void dontSecureProtectedFields() {
//		UUserAbs user = new UUserAbs();
//		user.setProtectedFields("imageUrls");
//		user.setAssociate(true);
//		user.setImageUrls(Arrays.asList(new String[] {"Dummy.com"}));
//		user.clean();
//		assertTrue(user.getImageUrls() != null);
//	}
//
//	@Test
//	public void preventProtectingRequiredFields() {
//		UUserAbs user = new UUserAbs();
//		try {
//			user.setRequiredFields("imageUrls");
//			user.setPrivateFields("imageUrls");
//			fail("Trying to set a required field as protected should have thrown an error");
//		} catch (Error e) {
//			assertTrue(true);
//		}
//
//		try {
//			user.setProtectedFields("imageUrls");
//			fail("Trying to set a required field as protected should have thrown an error");
//		} catch (Error e) {
//			assertTrue(true);
//		}
//	}
}
