package com.userSrvc.client.services;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.repo.UserBaseRepository;

public interface UserSrvcExt <U extends UUserAbs> extends UserSrvc<U> {
	public UserBaseRepository<U> getRepo();
}
