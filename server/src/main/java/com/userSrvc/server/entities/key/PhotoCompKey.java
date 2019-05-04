package com.userSrvc.server.entities.key;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

@Embeddable
@Table
@ManagedBean
@ApplicationScope
@Data
public class PhotoCompKey {
	@Column
	private long id;
	
	@Column
	private Byte[] photo;
}
