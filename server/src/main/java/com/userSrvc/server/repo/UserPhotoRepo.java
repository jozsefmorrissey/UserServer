package com.userSrvc.server.repo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.server.entities.UserPhoto;

public interface UserPhotoRepo extends JpaRepository<UserPhoto, Long> {

	List<UserPhoto> getByUserId(long id);
}

