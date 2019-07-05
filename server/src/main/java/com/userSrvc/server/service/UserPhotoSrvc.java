package com.userSrvc.server.service;

import java.io.FileNotFoundException;
import java.util.List;

import com.userSrvc.server.entities.UserPhoto;

public interface UserPhotoSrvc {
	public List<String> getUris(Long userId, Long appId);

	public Byte[] getPhoto(Long id) throws FileNotFoundException;

	public void updateOrder(List<Short> newOrder, Long userId, Long appId);
	
	public void update(UserPhoto userPhoto);
	public void update(List<UserPhoto> userPhoto);
}
