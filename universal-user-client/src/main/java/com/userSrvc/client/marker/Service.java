package com.userSrvc.client.marker;

import java.util.Collection;
import java.util.List;

public interface Service<T> {
	public T get(long id) throws Exception;
	public List<T> get(Collection<Long> ids) throws Exception;
	public T update(T obj) throws Exception;
}
