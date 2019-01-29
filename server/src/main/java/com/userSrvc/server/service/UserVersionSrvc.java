package com.userSrvc.server.service;

import com.userSrvc.server.entities.User;
import com.userSrvc.server.entities.UserVersion;

public interface UserVersionSrvc {
	public void update(UserVersion userVersion, User user) throws Exception;
	public String get(String email, String identifier) throws Exception;
}
