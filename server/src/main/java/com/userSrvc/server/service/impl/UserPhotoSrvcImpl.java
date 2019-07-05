package com.userSrvc.server.service.impl;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.server.entities.UserPhoto;
import com.userSrvc.server.repo.UserPhotoRepo;
import com.userSrvc.server.service.UserPhotoSrvc;

@Service
public class UserPhotoSrvcImpl implements UserPhotoSrvc {

	@Autowired
	UserPhotoRepo userPhotoRepo;

	public List<UserPhoto> getPhotos(Long userId, Long appId) {
		List<UserPhoto> photos =  userPhotoRepo.getByUserIdAndAppUserIdOrderByPosition(userId, appId);
		return photos;
	}

	@Override
	public List<String> getUris(Long userId, Long appId) {
		List<String> uris = new ArrayList<String>();
		List<UserPhoto> photos = getPhotos(userId, appId);
		for (UserPhoto photo : photos) {
			String uri = "/user/photo/" + photo.getId() + "." + 
					photo.getExt();
			uris.add(uri);
		}
		return uris;
	}

	@Override
	public Byte[] getPhoto(Long id) throws FileNotFoundException {
		UserPhoto photo = userPhotoRepo.getOne(id);
		if (photo != null) {
			return photo.getPhoto();
		}
		throw new FileNotFoundException();
	}

	@Override
	public void updateOrder(List<Short> newOrder, Long userId, Long appId) {
		List<UserPhoto> photos = getPhotos(userId, appId);
		for (int i = 0; i < photos.size(); i += 1) {
			photos.get(i).setPosition(newOrder.get(i));
		}
		userPhotoRepo.saveAll(photos);
	}

	@Override
	public void update(UserPhoto userPhoto) {
		userPhotoRepo.saveAndFlush(userPhoto);
	}
	
	@Override
	public void update(List<UserPhoto> userPhoto) {
		userPhotoRepo.saveAll(userPhoto);
	}
}
