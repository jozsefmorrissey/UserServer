package com.userSrvc.server.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.server.entities.Photo;

public interface UserPhotoRepo extends JpaRepository<Photo, Long> {

	public List<Photo> getByUserId(long id);
	public List<Photo> getByUserIdAndAppUserIdOrderByPosition(long userId, long appId);
	public Photo getByUserIdAndAppUserIdAndId(long userId, Long appId, long id);
	public void deleteByUserIdAndAppUserId(long userId, long appUserId);
}

