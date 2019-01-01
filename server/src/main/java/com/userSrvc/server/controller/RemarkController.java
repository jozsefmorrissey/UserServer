package com.userSrvc.server.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.server.entities.Remark;
import com.userSrvc.server.service.RemarkSrvc;

@RestController
@RequestMapping("remark")
public class RemarkController {
	@Autowired
	RemarkSrvc remarkSrvc;
	
	@PostMapping
	public void addRemark(@RequestBody Remark remark) {
		remarkSrvc.addRemark(remark);
	}

	@GetMapping("/{id}")
	public Remark getRemark(long id) {
		return remarkSrvc.getRemark(id);
	}

	@GetMapping("/conversation/{id}")
	public List<Remark> getRemarks(long conversationId) {
		return remarkSrvc.getRemarks(conversationId);
	}

}
