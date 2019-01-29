package com.userSrvc.server.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.server.entities.UserVersion;
import com.userSrvc.server.entities.key.UserPageIdentifierKey;

public interface UserVersionRepo extends JpaRepository<UserVersion, UserPageIdentifierKey> {

}
