package com.bookstore.dao.hibernate;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.bookstore.dao.HistoryDao;
import com.bookstore.entities.History;
import com.bookstore.entities.User;
import com.bookstore.utils.HibernateAction;

@Repository
public class HistoryDaoImplWithHibernate extends CommDaoImplWithHibernate<History> implements HistoryDao {
	public HistoryDaoImplWithHibernate() {
		super(History.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<History> getByUser(final User user) {
		Session session = sessionFactory.getCurrentSession();
		return session.createCriteria(History.class).add(Restrictions.eq("user", user)).addOrder(Order.desc("insertDate")).list();
	}
}
