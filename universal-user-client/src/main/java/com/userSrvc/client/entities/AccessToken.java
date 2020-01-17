package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;
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
@ManagedBean
@Table
@ApplicationScope
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccessToken {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long objectId;

	private String deviceIdentifier;

	private String token;
	private Long expiration;

	public AccessToken (Long objectId) {
		this(objectId, null, null);
	}
	
	public AccessToken (Long objectId, String deviceIdentifier) {
		this(objectId, deviceIdentifier, null);
		expiration = System.currentTimeMillis() + 300000;
	}
	
	public AccessToken (Long objectId, String deviceIdentifier, String token) {
		this.setObjectId(objectId);
		this.setDeviceIdentifier(deviceIdentifier);
		this.setToken(token);
	}
	

	public void setDeviceIdentifier(String deviceIdentifier) {
		if (deviceIdentifier == null || deviceIdentifier.equals("") ||
				deviceIdentifier.equals("undefined")) {
			deviceIdentifier = Util.randomString(32, "[A-Za-z0-9-|!@#$%^&*(]", ".*");
		}
		this.deviceIdentifier = deviceIdentifier;
	}

	public void setToken(String token) {
		if (token == null || token.equals("")) {
			token = Util.randomString(32, "[A-Za-z0-9-|!@#$%^&*(]", ".*");
		}
		this.token = token;
	}
	
	public boolean equals(Object obj) {
		if (! (obj instanceof AccessToken)) {
			return false;
		}

		AccessToken other = (AccessToken) obj;
		return other.deviceIdentifier.equals(deviceIdentifier) &&
				other.objectId.equals(objectId) && other.token.equals(token);
	}
}
