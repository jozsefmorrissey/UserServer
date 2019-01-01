package com.userSrvc.server.service;

import java.util.List;

import com.userSrvc.server.entities.Remark;

public interface RemarkSrvc {
	public void addRemark(Remark remark);
	public Remark getRemark(long id);
	public List<Remark> getRemarks(long conversationId);
}
