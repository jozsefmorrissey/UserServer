package com.userSrvc.server.entities.request;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.server.entities.UUser;

import lombok.Data;

@ManagedBean
@ApplicationScope
@Data
public class UserIdentifier {
	private String identifier;
	private UUser user;
}