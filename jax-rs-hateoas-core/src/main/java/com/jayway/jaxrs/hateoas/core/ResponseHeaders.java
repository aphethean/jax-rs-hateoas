package com.jayway.jaxrs.hateoas.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

public class ResponseHeaders<T> extends HashMap<String, List<T>> implements
		MultivaluedMap<String, T> {

	private static final long serialVersionUID = 2743101209228906279L;

	@Override
	public void putSingle(String key, T value) {
		List<T> values = getList(key);
		values.clear();
		values.add(value);
	}

	@Override
	public void add(String key, T value) {
		List<T> values = getList(key);
		values.add(value);
	}

	@Override
	public T getFirst(String key) {
		List<T> values = getList(key);
		return (values.size() > 0 ? values.get(0) : null);
	}

	protected List<T> getList(String key) {
		List<T> values = get(key);
		if (values == null) {
			values = new LinkedList<T>();
			put(key, values);
		}
		return values;
	}
}
