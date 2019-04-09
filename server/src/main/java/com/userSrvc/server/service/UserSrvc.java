package com.userSrvc.server.service;

import java.nio.file.AccessDeniedException;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import com.userSrvc.server.entities.User;

public interface UserSrvc {
	public void addUser(User user) throws ConstraintViolationException, PropertyValueException;
	
	public User loginUser(User user) throws PropertyValueException, DataException;
	
	public User authinticate(User user) throws AccessDeniedException;
	
	public User getUser(String email) throws PropertyValueException;
	
	
	public void updatePassword(User user) throws PropertyValueException, AccessDeniedException;
	
	
	public void updateEmail(User user, String newEmail) throws PropertyValueException, AccessDeniedException;
	
	
	public void resetPassword(User user, String url) throws PropertyValueException;
	
	public void update(User user) throws PropertyValueException, AccessDeniedException;
}
