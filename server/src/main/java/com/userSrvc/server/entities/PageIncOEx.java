package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.server.converter.IntBoolConverter;
import com.userSrvc.server.entities.key.UserPageIdentifierKey;

import lombok.Data;

/**
 * TODO: Implement a description(About Me) field
 * @author jozse
 *
 */
@Entity
@Table(name = "PAGE_INC_O_EX")
@ManagedBean
@ApplicationScope
@Data
public class PageIncOEx {
	@EmbeddedId
	private UserPageIdentifierKey id;
	
	@Column
	@Convert(converter = IntBoolConverter.class)
	private Boolean inclusion;
	
	public PageIncOEx() {}
	public PageIncOEx(UserPageIdentifierKey id) {
		this.id = id;
	}
	public PageIncOEx(UserPageIdentifierKey id, Boolean inclusion) {
		this.id = id;
		this.inclusion = inclusion;
	}
}
