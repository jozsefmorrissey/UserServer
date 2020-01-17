package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table
@ManagedBean
@ApplicationScope
@NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Data
public class AppAccessToken extends AccessToken{
	public AppAccessToken(ConnectionState connState) {
		super(connState.getAppId(), connState.getAppToken(), 
				connState.getDeviceId());
	}
	
	public AppAccessToken(Long appId) {
		super(appId);
	}
	
	public AppAccessToken(Long appId, String deviceId) {
		super(appId, deviceId);
	}

	public AppAccessToken(Long appId, String deviceId, String appToken) {
		super(appId, deviceId, appToken);
	}
}
