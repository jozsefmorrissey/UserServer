package com.userSrvc.server.repo;

import java.util.List;

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
	
	@Query(nativeQuery = true, value = "SELECT \n" + 
			"		@n ::= @n + 1 ID,\n" + 
			"		0 AS USER_ID, \n" + 
			"		0 AS APP_USER_ID, \n" + 
			"		REF_TYPE, \n" + 
			"		0 AS REF_ID, \n" + 
			"		\"\" AS TYPE, \n" + 
			"		0 AS ORIGIN_USER_ID,  \n" + 
			"		0 AS GRANTED_FROM_USER_ID\n" + 
			"	FROM \n" + 
			"    (select @n::=0) initvars,\n" + 
			"	(SELECT DISTINCT\n" + 
			"		REF_TYPE\n" + 
			"		FROM PERMISSION where USER_ID = :userId AND APP_USER_ID = :appUserId) AS T")
	public List<Permission> findDistinct(@Param(value = "userId") long userId, @Param(value = "appUserId") long appUserId);
	
	public List<Permission> getAllByUserIdAndAppUserId(long userId, long appId);
}
