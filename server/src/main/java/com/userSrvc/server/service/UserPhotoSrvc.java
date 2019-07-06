package com.userSrvc.server.service;

import java.io.FileNotFoundException;
import java.util.List;

import com.userSrvc.server.entities.Photo;

public interface UserPhotoSrvc {
	public List<String> getUris(Long userId, Long appId);

	public void updateOrder(List<Short> newOrder, Long userId, Long appId);
	
	public void update(Photo userPhoto);
	public void update(List<Photo> userPhoto);
}
