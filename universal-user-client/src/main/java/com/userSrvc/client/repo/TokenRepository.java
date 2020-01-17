package com.userSrvc.client.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.userSrvc.client.entities.AccessToken;

@NoRepositoryBean
public interface TokenRepository<T extends AccessToken> extends JpaRepository<T, Long> {}
