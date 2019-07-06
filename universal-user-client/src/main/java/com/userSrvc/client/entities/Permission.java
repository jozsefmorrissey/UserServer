package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.client.util.Util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PERMISSION")
@ManagedBean
@ApplicationScope
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Permission {
	// trasitive types

	// Un restricted modification access
	public static final String ADMIN = "admin";

	// Only original account owner, this can be transfered
	public static final String OAO = "oao";
	
	// Has all the access of the access of oao, 
	// confirmation is only required for granting 
	// access to another user.
	public static final String PSUEDO = "psuedo";
	
	// All modifications must be confirmed
	// no granting of privilages
	public static final String VALIDATION = "validation";

	// Cannot grant access
	public static final String NO = "NO";

	public static final Permission ROOT = new Permission(-1l, 0l, -1l, "__admin__", null, OAO, null, null);

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column
	private Long appUserId;

	@Column
	private Long userId;
	
	@Column
	private String refType;
	
	@Column
	private Long refId;

	@Column
	private String type;
	
	@Column
	private Long originUserId;

	@Column
	private Long grantedFromUserId;

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Permission)) {
			return false;
		}
		Permission pr = (Permission)obj;
		return Util.notNullAndEqualOrBothNull(this.refType, pr.refType) &&
				Util.notNullAndEqualOrBothNull(this.refId, pr.refId) &&
				Util.notNullAndEqualOrBothNull(this.appUserId, pr.appUserId);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAppUserId() {
		return appUserId;
	}

	public void setAppUserId(Long appUserId) {
		this.appUserId = appUserId;
	}

	public Long getUserId() {
		return userId;
	}

	public String getRefType() {
		return refType;
	}

	public void setRefType(String refType) {
		this.refType = refType;
	}

	public String getType() {
		return type;
	}

	public void setType(String transitive) {
		this.type = transitive;
	}

	public Long getOriginUserId() {
		return originUserId;
	}

	public void setOriginUserId(Long originUserId) {
		this.originUserId = originUserId;
	}

	public Long getRefId() {
		return refId;
	}

	public void setRefId(Long refId) {
		this.refId = refId;
	}

	public Long getGrantedFromUserId() {
		return grantedFromUserId;
	}

	public void setGrantedFromUserId(Long grantedFromUserId) {
		this.grantedFromUserId = grantedFromUserId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	} 
}
