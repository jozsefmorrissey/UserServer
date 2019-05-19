package com.userSrvc.server.entities;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.entities.UUserAbs;

import lombok.Data;


/**
 * TODO: Implement a description(About Me) field
 * @author jozse
 *
 */
@Entity
@Table
@ManagedBean
@ApplicationScope
@Data
public class UUser extends UUserAbs {

	public UUser() {
		super();
	}

	public UUser(long id, String name, String email, String password) {
		super(id, name, email, password, null);
		this.setId(id);
		this.setFullName(name);
		this.setEmail(email);
		this.setPassword(password);
//		this.setPhoto(ImageUtils.convertToBytes("src/main/resources/static/images/default-image.jpg"));
	}

	public UUser(long id, String name, String email, String password, byte[] photo) {
		super(id, name, email, password, photo);
	}
}
