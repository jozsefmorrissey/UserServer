package com.userSrvc.server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.server.entities.PageIncOEx;
import com.userSrvc.server.entities.key.UserPageIdentifierKey;

public interface PageIncOExRepo extends JpaRepository<PageIncOEx, UserPageIdentifierKey>{
	public List<PageIncOEx> findAllByIdPageIdentifier(String Identifier);
}
