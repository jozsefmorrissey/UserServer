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
	public static final String PUBLIC = "Public";
	public static final String PRIVATE = "Private";
	public static final String ASSOCIATE = "Associate";
	public static final String[] USER_SCOPES = {PUBLIC, ASSOCIATE, PRIVATE};
	
	public static final String ENCLUSIVE = "Enclusive";
	public static final String EXCLUSIVE = "Exclusive";
	public static final String[] APP_LISTS = {ENCLUSIVE, EXCLUSIVE};
	
	
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
