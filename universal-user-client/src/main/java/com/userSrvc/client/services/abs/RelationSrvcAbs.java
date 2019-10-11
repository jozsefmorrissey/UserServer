package com.userSrvc.client.services.abs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;

import com.userSrvc.client.entities.Relation;
import com.userSrvc.client.marker.RelationSrvc;

public abstract class RelationSrvcAbs <T, I> implements RelationSrvc<Long, Long> {
	
	public abstract JpaRepository<T, I> getRepo();
	public abstract T create(Long primary, Long secondary);

	@Override
	public void add(Long primaryId, Long secondaryId) {
		addAll(Arrays.asList(new Long[] {primaryId}), Arrays.asList(new Long[] {secondaryId}));
	}

	@Override
	public void delete(Long primaryId, Long secondaryId) {
		deleteAll(Arrays.asList(new Long[] {primaryId}), Arrays.asList(new Long[] {secondaryId}));
	}

	@Override
	public void addAll(Long primaryId, Collection<Long> secondaryIds) {
		addAll(Arrays.asList(new Long[] {primaryId}), secondaryIds);
	}

	@Override
	public void deleteAll(Long primaryId, Collection<Long> secondaryIds) {
		deleteAll(Arrays.asList(new Long[] {primaryId}), secondaryIds);
	}

	@Override
	public void addAll(Collection<Long> primaryIds, Long secondaryId) {
		addAll(primaryIds, Arrays.asList(new Long[] {secondaryId}));
	}

	@Override
	public void deleteAll(Collection<Long> primaryIds, Long secondaryId) {
		deleteAll(primaryIds, Arrays.asList(new Long[] {secondaryId}));
	}
	
	@Override
	public void deleteAll(Collection<Long> primaryIds, Collection<Long> secondaryIds) {
		List<T> compEmps = buildCrosList(primaryIds, secondaryIds);
		getRepo().deleteAll(compEmps);
	}

	@Override
	public void addAll(Collection<Long> primaryIds, Collection<Long> secondaryIds) {
		List<T> compEmps = buildCrosList(primaryIds, secondaryIds);
		getRepo().saveAll(compEmps);
	}

	@Override
	public void deleteAll(Collection<Relation<Long, Long>> mappedIds) {
		List<T> compEmps = buildList(mappedIds);
		getRepo().deleteAll(compEmps);
	}

	@Override
	public void addAll(Collection<Relation<Long, Long>> mappedIds) {
		List<T> compEmps = buildList(mappedIds);
		getRepo().saveAll(compEmps);
	}

	protected List<T> buildList(Map<Long, Long> mappedIds) {
		List<T> companyFacilities = new ArrayList<T>();
		for (Long primaryId : mappedIds.keySet()) {
			companyFacilities.add(create(primaryId, mappedIds.get(primaryId)));
		}
		return companyFacilities;
	}

	protected List<T> buildList(Collection<Relation<Long, Long>> mappedIds) {
		List<T> companyFacilities = new ArrayList<T>();
		for (Relation<Long, Long> rel : mappedIds) {
			companyFacilities.add(create(rel.getPrimary(), rel.getSecondary()));
		}
		return companyFacilities;
	}

	protected List<T> buildCrosList(Collection<Long> primaryIds, Collection<Long> secondaryIds) {
		List<T> companyFacilities = new ArrayList<T>();
		for (Long secondaryId : secondaryIds) {
			for (Long primaryId : primaryIds) {
				companyFacilities.add(create(primaryId, secondaryId));
			}
		}
		return companyFacilities;
	}
}
