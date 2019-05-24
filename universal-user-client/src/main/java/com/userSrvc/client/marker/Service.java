package com.userSrvc.client.marker;

import java.util.Collection;
import java.util.List;

public interface Service<T> {
	public T get(T obj) throws Exception;
	public List<T> getAll(Collection<Long> ids) throws Exception;
}
