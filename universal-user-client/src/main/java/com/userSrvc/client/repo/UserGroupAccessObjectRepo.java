package com.userSrvc.client.repo;

import org.springframework.data.repository.NoRepositoryBean;

import com.userSrvc.client.entities.access.GroupObjectAccess;

@NoRepositoryBean
public interface UserGroupAccessObjectRepo extends AccessObjectBaseRepository<GroupObjectAccess> {}
