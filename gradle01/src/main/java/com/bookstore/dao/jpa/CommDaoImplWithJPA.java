package com.bookstore.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;

import com.bookstore.dao.CommDao;

public class CommDaoImplWithJPA<T> implements CommDao<T> {
	@PersistenceContext
	protected EntityManager em;
	
	private final Class<T> type;
	
	public CommDaoImplWithJPA(Class<T> type) {
		this.type = type;
	}
	
	@Override
	public void add(T obj) {
		em.persist(obj);
	}

	@Override
	public T getById(Object id) {
		return (T) em.find(type, id);
	}

	@Override
	public void update(T obj) {
		em.persist(obj);
	}

	@Override
	public List<T> getAll() {
		CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(type);
		cq.from(type);
		return em.createQuery(cq).getResultList();
	}

	@Override
	public int countAll() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		return em.createQuery(cq.select(cb.count(cq.from(type)))).getSingleResult().intValue();
	}

	@Override
	public void deleteAll() {
		CriteriaDelete<T> cd = em.getCriteriaBuilder().createCriteriaDelete(type);
		cd.from(type);
		em.createQuery(cd).executeUpdate();
	}

}
