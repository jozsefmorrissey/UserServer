package com.userSrvc.server.entities.request;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.server.entities.User;
import com.userSrvc.server.entities.UserVersion;

import lombok.Data;

@ManagedBean
@ApplicationScope
@Data
public class UserVersionUser {
	private UserVersion userVersion;
	private User user;
}
