package com.userSrvc.server.entities.request;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import com.userSrvc.server.entities.User;

import lombok.Data;

@ManagedBean
@ApplicationScope
@Data
public class UserTopic {
	private String topic;
	private User user;
}
