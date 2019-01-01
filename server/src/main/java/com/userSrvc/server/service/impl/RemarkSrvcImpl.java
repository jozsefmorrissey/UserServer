package com.userSrvc.server.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userSrvc.server.entities.Remark;
import com.userSrvc.server.repo.RemarkRepo;
import com.userSrvc.server.service.RemarkSrvc;

@Service
public class RemarkSrvcImpl implements RemarkSrvc {

	@Autowired
	RemarkRepo remarkRepo;
	
	@Override
	public void addRemark(Remark remark) {
		remarkRepo.save(remark);
	}

	@Override
	public Remark getRemark(long id) {
		return remarkRepo.getById(id);
	}

	@Override
	public List<Remark> getRemarks(long conversationId) {
		return remarkRepo.getByConversationIdOrderByTimeStamp(conversationId);
	}

}
