package com.userSrvc.server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.client.entities.Rule;

public interface RuleRepo extends JpaRepository<Rule, Long> {
	public List<Rule> findByAppId(long appId);
	public void deleteByAppId(long appId);
}
