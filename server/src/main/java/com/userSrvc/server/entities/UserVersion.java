package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.server.converter.StringByteConverter;
import com.userSrvc.server.entities.key.UserPageIdentifierKey;

import lombok.Data;

@Entity
@Table
@ManagedBean
@ApplicationScope
@Data
public class UserVersion {
	@EmbeddedId
	UserPageIdentifierKey id;
	
	@Column
	@Convert(converter = StringByteConverter.class)
	private String jsonObj;
}
