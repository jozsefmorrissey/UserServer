package com.userSrvc.server.repo;


import org.springframework.transaction.annotation.Transactional;

import com.userSrvc.client.repo.UserBaseRepository;
import com.userSrvc.server.entities.UUser;

@Transactional
public interface UserRepo extends UserBaseRepository<UUser> {
}

