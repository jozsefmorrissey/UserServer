package com.userSrvc.server.entities;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.Data;

@ManagedBean
@ApplicationScope
@Data
public class UserUrl {
	private String url;
	private User user;
}
