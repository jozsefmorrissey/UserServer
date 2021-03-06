package com.userSrvc.server.service;

import com.userSrvc.server.entities.User;

public interface UserSrvc {
	public void addUser(User user) throws Exception;
	public User loginUser(User user) throws Exception;
	public User authinticate(User user) throws Exception;
	public User getUser(String email) throws Exception;
	public void updatePassword(User user) throws Exception;
	public void updateEmail(User user, String newEmail) throws Exception;
	void resetPassword(User user, String url) throws Exception;
}
