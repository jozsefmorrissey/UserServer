package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.server.converter.IntBoolConverter;
import com.userSrvc.server.utils.ImageUtils;

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
public class User {
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_ID_SEQ")
	@SequenceGenerator(name = "USER_ID_SEQ", sequenceName = "USER_ID_SEQ")
	private long id;

	@Column
	private String name;

	@Column
	private String email;

	@Column
	private String userToken;

	@Column
	private String password;


	@Column
	@Convert(converter = IntBoolConverter.class)
	private Boolean cantModify;

	@Column
	private byte[] photo;

	public User() {
		super();
	}

	public User(long id, String name, String email, String password) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.photo = ImageUtils.convertToBytes("src/main/resources/static/images/default-image.jpg");
	}

	public User(long id, String name, String email, String password, byte[] photo) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.photo = photo;
	}


}
