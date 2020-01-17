package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.entities.UUserAbs;

import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * TODO: Implement a description(About Me) field
 * @author jozse
 *
 */
@Entity(name = "UUser")
@Table
@ManagedBean
@ApplicationScope
@EqualsAndHashCode(callSuper=true)
@Data
public class UUser extends UUserAbs {

	public UUser() {
		super();
	}

	public UUser(UUserAbs user) {
		super(user);
	}

	public UUser(long id, String name, String email, String password) {
		super(id, name, email, password, null);
		this.setId(id);
		this.setFullname(name);
		this.setEmail(email);
		this.setPassword(password);
//		this.setPhoto(ImageUtils.convertToBytes("src/main/resources/static/images/default-image.jpg"));
	}

	public UUser(long id, String name, String email, String password, byte[] photo) {
		super(id, name, email, password, photo);
	}
}
