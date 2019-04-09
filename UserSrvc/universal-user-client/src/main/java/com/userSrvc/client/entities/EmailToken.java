package com.userSrvc.client.entities;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.ApplicationScope;

import lombok.AllArgsConstructor;
import lombok.Data;

@ManagedBean
@ApplicationScope
@AllArgsConstructor
@Data
public class EmailToken {
	private String email;
	private String userToken;
}
