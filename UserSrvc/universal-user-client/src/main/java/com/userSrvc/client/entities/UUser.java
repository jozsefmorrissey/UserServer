package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

/**
 * TODO: Implement a description(About Me) field
 * @author jozse
 *
 */
@ManagedBean
@ApplicationScope
@Data
public class UUser {
	private long id;
	private String name;
	private String email;
	private String userToken;
	private String password;
	private byte[] photo;

	public UUser() {
		super();
	}

	public UUser(long id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.photo = null;//ImageUtils.convertToBytes("src/main/resources/static/images/default-image.jpg");
	}

	public UUser(long id, String name, String email, String password, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.photo = photo;
	}
	
	
}
