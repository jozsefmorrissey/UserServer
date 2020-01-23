package com.userSrvc.client.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.userSrvc.client.entities.access.ObjectAccess;

@NoRepositoryBean
public interface AccessObjectBaseRepository<A extends ObjectAccess<?>> extends JpaRepository<A, Long> {
	public List<ObjectAccess<?>> findByobjectTypeValueAndObjectId(String objectType, Long objectId);
}
