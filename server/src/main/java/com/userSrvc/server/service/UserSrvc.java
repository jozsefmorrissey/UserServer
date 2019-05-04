package com.userSrvc.server.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.DataException;

import com.userSrvc.exceptions.StatisticallyImpossible;
import com.userSrvc.server.entities.UUser;

public interface UserSrvc {
	public UUser addUser(UUser user) throws ConstraintViolationException, PropertyValueException, StatisticallyImpossible;
	
	public UUser loginUser(UUser user) throws PropertyValueException, DataException;
	
	public UUser authinticate(UUser user) throws AccessDeniedException;
	
	public UUser getUser(UUser user) throws PropertyValueException;

	public void updatePassword(UUser user) throws PropertyValueException, AccessDeniedException;
	
	
	public void updateEmail(UUser user, String newEmail) throws PropertyValueException, AccessDeniedException;
	
	
	public void resetPassword(UUser user, String url) throws PropertyValueException;
	
	public void update(UUser user) throws PropertyValueException, AccessDeniedException;
	
	public List<Byte[]> photo(long id);
}
