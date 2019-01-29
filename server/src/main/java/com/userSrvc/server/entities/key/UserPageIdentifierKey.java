package com.userSrvc.server.entities.key;

import java.io.Serializable;

import javax.annotation.ManagedBean;
import javax.persistence.Embeddable;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

@Embeddable
@Table
@ManagedBean
@ApplicationScope
@Data
public class UserPageIdentifierKey implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1494030072934124350L;
	
	private long userId;

	private String pageIdentifier;	
	
	public UserPageIdentifierKey() {}
	public UserPageIdentifierKey(long userId, String pageIdentifier) {
		this.userId = userId;
		this.pageIdentifier = pageIdentifier;
	}
	
}
