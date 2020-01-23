package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.constant.ToJson;
import com.userSrvc.server.entities.key.FieldScopeKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@ManagedBean
@ApplicationScope
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
public class FieldScope extends ToJson {
	@Id
	private FieldScopeKey key;
	
	@Column
	private String field;
	
	@Column
	private String scope;

	@Column
	private String defaultScope;
	
	public boolean incompleteKey() {
		return key.incomplete();
	}
}