package com.userSrvc.client.entities;

import java.util.List;

import lombok.Data;

@Data
public class RelationLists<P, S> {
	private List<P> primaryList;
	private List<S> secondaryList;
}
