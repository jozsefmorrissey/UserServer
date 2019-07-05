package com.userSrvc.server.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.exceptions.StatisticallyImpossible;

public interface UserSrvc {
	public UUserAbs addUser(UUserAbs user) throws ConstraintViolationException, PropertyValueException, StatisticallyImpossible;
	
	public UUserAbs loginUser(UUserAbs user) throws PropertyValueException, DataException;
	
	public UUserAbs authinticate(UUserAbs user) throws AccessDeniedException;
	
	public UUserAbs getUser(long id) throws PropertyValueException;
	public UUserAbs getUser(String email) throws PropertyValueException;

	public void updatePassword(UUserAbs user) throws PropertyValueException, AccessDeniedException;
	
	
	public void updateEmail(UUserAbs user, String newEmail) throws PropertyValueException, AccessDeniedException;
	
	
	public void resetPassword(UUserAbs user, String url) throws PropertyValueException;
	
	public void update(UUserAbs user) throws PropertyValueException, AccessDeniedException;
	
	public List<UUserAbs> getUsers(List<Long> ids);

	List<UUserAbs> cleanUsers(List<UUserAbs> users);
}
