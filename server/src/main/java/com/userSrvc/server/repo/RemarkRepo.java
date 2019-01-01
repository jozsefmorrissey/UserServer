package com.userSrvc.server.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.server.entities.Remark;

public interface RemarkRepo extends JpaRepository<Remark, Long> {
	Remark getById(long id);
	List<Remark> getByConversationIdOrderByTimeStamp(long id);
}
