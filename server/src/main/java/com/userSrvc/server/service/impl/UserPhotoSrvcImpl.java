package com.userSrvc.server.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.server.entities.Photo;
import com.userSrvc.server.repo.UserPhotoRepo;
import com.userSrvc.server.service.UserPhotoSrvc;

@Service
public class UserPhotoSrvcImpl implements UserPhotoSrvc {

	@Autowired
	UserPhotoRepo userPhotoRepo;

	public List<Photo> getPhotos(Long userId, Long appId) {
		List<Photo> photos =  userPhotoRepo.getByObjectIdAndAppUserIdOrderByPosition(userId, appId);
		return photos;
	}

	@Override
	public List<String> getUris(Long userId, Long appId) {
		List<String> uris = new ArrayList<String>();
		List<Photo> photos = getPhotos(userId, appId);
		for (Photo photo : photos) {
			uris.add(photo.getUrl());
		}
		return uris;
	}

	@Override
	public void updateOrder(List<Short> newOrder, Long userId, Long appId) {
		List<Photo> photos = getPhotos(userId, appId);
		for (int i = 0; i < photos.size(); i += 1) {
			photos.get(i).setPosition(newOrder.get(i));
		}
		userPhotoRepo.saveAll(photos);
	}

	@Override
	public void update(Photo userPhoto) {
		userPhotoRepo.saveAndFlush(userPhoto);
	}
	
	@Override
	public void update(List<Photo> userPhoto) {
		userPhotoRepo.saveAll(userPhoto);
	}
}
