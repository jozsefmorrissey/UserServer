package com.userSrvc.client.entities;

import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.OneToMany;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

/**
 * TODO: Implement a description(About Me) field
 * @author jozse
 *
 */
@Entity
@ManagedBean
@ApplicationScope
@Data
@Inheritance
public class UUserAbs {
	@Id
	private Long id;
	
	@Column
	private String fullName;

	@Column
	private String email;
	
	@Column
	private String userToken;

	@Column
	private String password;
	
	@OneToMany(targetEntity=Permission.class, fetch = FetchType.LAZY, mappedBy = "userId")
	private List<Permission> permissions;

	public UUserAbs() {
		super();
	}
	
	public UUserAbs(long id, String name, String email, String password, byte[] photo) {
		super();
		this.setId(id);
		this.setFullName(name);
		this.setEmail(email);
		this.setPassword(password);
	}

	public void merge(UUserAbs dbUser) {
		this.setId(dbUser.getId());
		this.setFullName(dbUser.getFullName());
		this.setUserToken(dbUser.getUserToken());
		this.setEmail(dbUser.getEmail());
	}
	
	public String getName(int i) {
		if (fullName == null) {
			return null;
		}
		String[] names = fullName.split(" ");
		if (i >= names.length - 1) {
			return names[names.length - 1];
		}
		if (i <= 0) {
			return names[0];
		}
		return names[i];
	}
}
