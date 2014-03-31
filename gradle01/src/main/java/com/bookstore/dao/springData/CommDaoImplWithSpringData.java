package com.bookstore.dao.springData;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bookstore.dao.CommDao;

public class CommDaoImplWithSpringData<T> implements CommDao<T> {
	@Autowired
	protected JpaRepository<T, Serializable> repository;
	
	@Override
	public void add(T obj) {
		repository.save(obj);
	}

	@Override
	public T getById(Object id) {
		return repository.findOne((Serializable)id);
	}

	@Override
	public void update(T obj) {
		repository.save(obj);
	}

	@Override
	public List<T> getAll() {
		return repository.findAll();
	}

	@Override
	public int countAll() {
		return ((Long) repository.count()).intValue();
	}

	@Override
	public void deleteAll() {
		repository.deleteAll();
	}
}
