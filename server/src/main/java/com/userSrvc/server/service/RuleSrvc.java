package com.userSrvc.server.service;

import java.nio.file.AccessDeniedException;
import java.util.List;

import com.userSrvc.client.entities.Rule;

public interface RuleSrvc {
	public List<Rule> getRulesByAppId(long appId);
	public void updateRules(List<Rule> rules) throws AccessDeniedException;
}
