package com.userSrvc.server.repo;


import org.springframework.transaction.annotation.Transactional;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.repo.UserBaseRepository;

@Transactional
public interface UserRepo extends UserBaseRepository<UUserAbs> {
}

