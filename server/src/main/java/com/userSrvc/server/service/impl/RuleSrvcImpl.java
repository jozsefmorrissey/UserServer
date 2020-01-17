package com.userSrvc.server.service.impl;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.userSrvc.client.aop.AopAuth;
import com.userSrvc.client.entities.App;
import com.userSrvc.client.entities.Rule;
import com.userSrvc.client.error.ERROR_MSGS;
import com.userSrvc.server.entities.UUser;
import com.userSrvc.server.repo.RuleRepo;
import com.userSrvc.server.service.RuleSrvc;

public class RuleSrvcImpl implements RuleSrvc {

	@Autowired
	RuleRepo ruleRepo;
	
	@Autowired
	AopAuth<UUser, App> aopAuth;
	
	@Override
	public List<Rule> getRulesByAppId(long appId) {
		return ruleRepo.findByAppId(appId);
	}

	@Override
	public void updateRules(List<Rule> rules) throws AccessDeniedException {
		App app = aopAuth.appRequrired();
		for (Rule rule : rules) {
			if (!app.getId().equals(rule.getAppId())) {
				throw new AccessDeniedException(ERROR_MSGS.INCORRECT_APP_CREDENTIALS);
			}
		}
		ruleRepo.deleteByAppId(app.getId());
		ruleRepo.saveAll(rules);
	}

}
