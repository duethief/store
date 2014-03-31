package com.bookstore.dao;

import java.util.List;

public interface CommDao <T> {
	void add(T obj);
	T getById(Object id);
	void update(T obj);
	List<T> getAll();
	int countAll();
	void deleteAll();
}
