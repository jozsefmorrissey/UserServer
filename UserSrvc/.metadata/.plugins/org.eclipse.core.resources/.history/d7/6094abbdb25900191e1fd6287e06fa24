package com.userSrvc.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.server.entities.User;
import com.userSrvc.server.entities.UserVersion;
import com.userSrvc.server.entities.key.UserPageIdentifierKey;
import com.userSrvc.server.repo.UserVersionRepo;
import com.userSrvc.server.service.UserSrvc;
import com.userSrvc.server.service.UserVersionSrvc;
import com.userSrvc.server.utils.StringUtils;

@Service
public class UserVersionSrvcImpl implements UserVersionSrvc {

	@Autowired
	UserVersionRepo uvr;
	
	@Autowired
	UserSrvc userSrvc;
	
	@Override
	public void update(UserVersion userVersion, User user) throws Exception {
		User dbUser = userSrvc.authinticate(user);
		if (userVersion.getId().getPageIdentifier() == null) {
			throw new Exception("Page Id is not defined");
		}
		userVersion.getId().setUserId(dbUser.getId());
		userVersion.setJsonObj(StringUtils.cleanHtml(userVersion.getJsonObj()));
		uvr.save(userVersion);

	}

	@Override
	public String get(String email, String identifier) throws Exception {		
		User user = userSrvc.getUser(email);
		UserPageIdentifierKey uvk = new UserPageIdentifierKey();
		uvk.setPageIdentifier(identifier);
		uvk.setUserId(user.getId());
		return uvr.getOne(uvk).getJsonObj();
	}

}
