package com.userSrvc.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.server.entities.Page;
import com.userSrvc.server.entities.PageIncOEx;
import com.userSrvc.server.entities.User;
import com.userSrvc.server.repo.PageIncOExRepo;
import com.userSrvc.server.repo.PageRepo;
import com.userSrvc.server.service.PageSrvc;
import com.userSrvc.server.service.UserSrvc;
import com.userSrvc.server.utils.StringUtils;

@Service
public class PageSrvcImpl implements PageSrvc {

	@Autowired
	PageRepo pageRepo;

	@Autowired
	PageIncOExRepo pageIncOExRepo;
	
	@Autowired
	UserSrvc userSrvc;
	
	@Override
	public void update(Page page, User user) throws Exception {
		if (page.getIdentifier() == null) {
			throw new Exception("Page Identifier is not defined");
		}
		isAuthorized(user, page.getIdentifier());
		Page dbPage = pageRepo.getByIdentifier(page.getIdentifier());
		if (dbPage == null) {
			dbPage = page;
		}
		dbPage.setJsonObj(StringUtils.cleanHtml(page.getJsonObj()));
		System.out.println(dbPage.getJsonObj());
		pageRepo.saveAndFlush(dbPage);
	}

	@Override
	public String get(String identifier) {
		System.out.println("IDENTIFIER: " + identifier);
		Page page = pageRepo.getByIdentifier(identifier);
		if (page != null) {
			return page.getJsonObj();
		} else {
			return "";
		}
	}

	@Override
	public Boolean isAuthorized(User user, String topic) throws Exception {
		User dbUser = userSrvc.authinticate(user);
		if (dbUser.getCantModify().booleanValue()) {
			throw new Exception("Access Denied");
		}
		
		List<PageIncOEx> pioes = pageIncOExRepo.findAllByIdPageIdentifier(topic);
		boolean hasInclusion = false;
		for (PageIncOEx pioe : pioes) {
			if (pioe.getInclusion().booleanValue()) {
				if (dbUser.getId() == pioe.getId().getUserId()) {
					return true;
				}
				hasInclusion = true;
			} else {
				if (dbUser.getId() == pioe.getId().getUserId()) {
					throw new Exception("Access Denied");
				}
			}
		}
		
		if (hasInclusion) {
			throw new Exception("Access Denied");
		}
		
		return true;
	}
}
