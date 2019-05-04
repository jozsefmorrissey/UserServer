package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

@ManagedBean
@ApplicationScope
@Data
public class UserUrl <U extends UUserAbs> {
	private String url;
	private U user;
}
