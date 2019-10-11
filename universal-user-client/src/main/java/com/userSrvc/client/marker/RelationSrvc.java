package com.userSrvc.client.marker;

import java.util.Collection;

import com.userSrvc.client.entities.Relation;

public interface RelationSrvc<P, S> {
	public void add(P primaryId, S secondaryId);
	public void delete(P primaryId, S secondaryId);
	public void addAll(P primaryId, Collection<S> secondaryIds);
	public void deleteAll(P primaryId, Collection<S> secondaryIds);
	public void addAll(Collection<P> primaryIds, S secondaryId);
	public void deleteAll(Collection<P> primaryIds, S secondaryId);
	public void addAll(Collection<P> primaryIds, Collection<S> secondaryId);
	public void deleteAll(Collection<P> primaryIds, Collection<S> secondaryId);
	public void addAll(Collection<Relation<P, S>> mappedIds);
	public void deleteAll(Collection<Relation<P, S>> mappedIds);
}
