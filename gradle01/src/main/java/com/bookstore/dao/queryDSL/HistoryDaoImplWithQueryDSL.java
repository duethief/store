package com.bookstore.dao.queryDSL;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.HistoryDao;
import com.bookstore.entities.History;
import com.bookstore.entities.QHistory;
import com.bookstore.entities.User;
import com.mysema.query.jpa.impl.JPAQuery;

@Repository
public class HistoryDaoImplWithQueryDSL extends CommDaoImplWithQueryDSL<History> implements HistoryDao {
	public HistoryDaoImplWithQueryDSL() {
		super(History.class);
	}

	@Override
	public List<History> getByUser(User user) {
		return new JPAQuery(em).from(q).where(QHistory.history.user.eq(user)).list(q);
	}
}
