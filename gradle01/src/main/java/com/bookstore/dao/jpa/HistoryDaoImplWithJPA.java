package com.bookstore.dao.jpa;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.HistoryDao;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

@Repository
public class HistoryDaoImplWithJPA extends CommDaoImplWithJPA<History> implements HistoryDao {
	public HistoryDaoImplWithJPA() {
		super(History.class);
	}
	
	@Override
	public List<History> getByUser(final User user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<History> cq = cb.createQuery(History.class);
		return em.createQuery(cq.where(cb.equal(cq.from(History.class).get("user"), user))).getResultList();
	}
}
