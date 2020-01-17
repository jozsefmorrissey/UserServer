package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@ManagedBean
@Table
@ApplicationScope
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Data
public class UserAccessToken extends AccessToken {
	public UserAccessToken(ConnectionState connState) {
		super(connState.getUserId(), connState.getAppToken(), 
				connState.getDeviceId());
	}
	
	public UserAccessToken(Long userId) {
		super(userId);
	}
	
	public UserAccessToken(Long userId, String deviceId) {
		super(userId, deviceId);
	}

	public UserAccessToken(Long userId, String deviceId, String appToken) {
		super(userId, deviceId, appToken);
	}
}
