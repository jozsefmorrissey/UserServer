package com.userSrvc.client.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.userSrvc.client.entities.App;

@NoRepositoryBean
public interface AppBaseRepository<A extends App> extends JpaRepository<A, Long> {}
