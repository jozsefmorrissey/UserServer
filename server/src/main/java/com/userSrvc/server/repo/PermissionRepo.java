package com.userSrvc.server.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.userSrvc.client.entities.Permission;

public interface PermissionRepo extends JpaRepository<Permission, Long> {

	@Transactional
	@Modifying(flushAutomatically=true,clearAutomatically=true)
	@Query(nativeQuery = true, value = "INSERT INTO PERMISSION (\n" + 
			"	ID,\n" + 
			"	USER_ID,\n" + 
			"	APP_USER_ID,\n" + 
			"	REF_TYPE,\n" + 
			"	REF_ID,\n" + 
			"	TYPE,\n" + 
			"	ORIGIN_USER_ID\n" + 
			") SELECT 0,\n" + 
			"	USER_ID,\n" + 
			"	APP_USER_ID,\n" + 
			"	:newRefType,\n" + 
			"	:newId,\n" + 
			"	:newType,\n" + 
			"	ORIGIN_USER_ID\n" + 
			"	FROM PERMISSION\n" + 
			"    WHERE REF_TYPE = :oldType AND \n" + 
			"		REF_ID = :oldId AND \n" + 
			"		APP_USER_ID = :appId")
	public void cascade(@Param(value = "newRefType") String newRefType,
			@Param(value = "newId") Long newId,
			@Param(value = "oldType") String oldType,
			@Param(value = "oldId") Long oldId,
			@Param(value = "appId") Long appId,
			@Param(value = "newType") String newType);
	
	public Permission getByRefTypeAndRefId(String refType, Long refId);
}
