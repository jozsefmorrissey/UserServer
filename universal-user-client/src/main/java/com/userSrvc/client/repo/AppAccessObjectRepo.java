package com.userSrvc.client.repo;

import org.springframework.data.repository.NoRepositoryBean;

import com.userSrvc.client.entities.access.AppObjectAccess;

@NoRepositoryBean
public interface AppAccessObjectRepo extends AccessObjectBaseRepository<AppObjectAccess> {}
