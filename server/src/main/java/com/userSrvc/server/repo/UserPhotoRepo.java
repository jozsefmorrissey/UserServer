package com.userSrvc.server.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.server.entities.Photo;

public interface UserPhotoRepo extends JpaRepository<Photo, Long> {

	public List<Photo> getByObjectId(long id);
	public List<Photo> getByObjectIdAndAppUserIdOrderByPosition(long userId, long appId);
	public Photo getByObjectIdAndAppUserIdAndId(long userId, Long appId, long id);
}

