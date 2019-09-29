package com.userSrvc.server.repo;


import java.util.Collection;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.userSrvc.client.entities.UUserAbs;
import com.userSrvc.client.repo.UserBaseRepository;

@Transactional
public interface UserRepo extends UserBaseRepository<UUserAbs> {
	public List<UUserAbs> findByEmailIn(Collection<String> email);
}

