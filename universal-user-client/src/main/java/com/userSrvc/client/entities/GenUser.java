package com.userSrvc.client.entities;

public class GenUser extends UUserAbs {
	public GenUser() {}
	
	public GenUser(long id, String name, String email, String password) {
		this(id, name, email, password, null);
	}

	public GenUser(long id, String name, String email, String password, byte[] photo) {
		super();
		this.setFullName(name);
		this.setEmail(email);
		this.setPassword(password);
//		this.setPhoto(photo);//ImageUtils.convertToBytes("src/main/resources/static/images/default-image.jpg");
	}
}
