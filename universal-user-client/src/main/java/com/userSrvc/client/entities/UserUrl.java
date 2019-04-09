package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

@ManagedBean
@ApplicationScope
@Data
public class UserUrl <U extends UUser> {
	private String url;
	private U user;
}
