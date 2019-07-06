package com.userSrvc.server.controller;

import java.nio.file.AccessDeniedException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.userSrvc.client.constant.URI;
import com.userSrvc.client.entities.ApplicationPermissionRequest;
import com.userSrvc.client.entities.Permission;
import com.userSrvc.client.services.PermissionSrvc;

@CrossOrigin
@RestController
@RequestMapping
public class PermissionCtrl {
	@Autowired
	PermissionSrvc permSrvc;
	
	@GetMapping(URI.PERMISSION_GET_USER_ID)
	public List<Permission> get(@PathVariable long userId) {
		return permSrvc.get(userId, 1);
	}
	
	@PostMapping(URI.PERMISSION_ADD)
	public void add(@RequestBody ApplicationPermissionRequest pr) throws AccessDeniedException {
		permSrvc.add(pr);
	}

	@PostMapping(URI.PERMISSION_ADD_ALL)
	public void add(@RequestBody Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException {
		permSrvc.addAll(prs);
	}

	@PostMapping(URI.PERMISSION_REMOVE)
	public void remove(@RequestBody ApplicationPermissionRequest pr) throws AccessDeniedException {
		permSrvc.remove(pr);
	}

	@PostMapping(URI.PERMISSION_REMOVE_ALL)
	public void remove(@RequestBody Collection<ApplicationPermissionRequest> prs) throws AccessDeniedException {
		permSrvc.removeAll(prs);
	}
}
