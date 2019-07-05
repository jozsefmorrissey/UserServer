package com.userSrvc.client.entities;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
	Collection<PermissionRequest> add;
	Collection<PermissionRequest> remove;
	public Collection<PermissionRequest> getAdd() {
		return add;
	}
	public void setAdd(Collection<PermissionRequest> add) {
		this.add = add;
	}
	public Collection<PermissionRequest> getRemove() {
		return remove;
	}
	public void setRemove(Collection<PermissionRequest> remove) {
		this.remove = remove;
	}
}
