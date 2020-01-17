package com.userSrvc.server.entities.key;

import java.io.Serializable;

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
public class FieldScopeKey implements Serializable {
	private static final long serialVersionUID = -5315198775982947433L;
	
	@Column
	private Long userId;

	@Column
	private Long appId;
	
	@Column
	private Long objectNameId;

	@Column
	private Long fieldNameId;
	
	public boolean incomplete() {
		return userId == null || appId == null || objectNameId == null
				|| fieldNameId == null;
	}
}
