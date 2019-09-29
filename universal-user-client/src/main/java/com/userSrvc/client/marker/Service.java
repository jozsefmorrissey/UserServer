package com.userSrvc.client.marker;

import java.util.Collection;
import java.util.List;

public interface Service<T, I> {
	public T get(I id) throws Exception;
	public List<T> get(Collection<I> emails) throws Exception;
	public T update(T obj) throws Exception;
}
