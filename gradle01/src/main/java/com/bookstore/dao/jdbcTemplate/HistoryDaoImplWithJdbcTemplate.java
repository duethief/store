package com.bookstore.dao.jdbcTemplate;

import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.bookstore.dao.HistoryDao;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

@Repository
public class HistoryDaoImplWithJdbcTemplate extends CommDaoImplWithJdbcTemplate<History> implements HistoryDao {
	private final String SQL_GET_BY_USER = "select id, userId, bookId, actionType, insertDate from Histories where userId = ?";
	
	public HistoryDaoImplWithJdbcTemplate() {
		super(History.class);
	}

	@Override
	public List<History> getByUser(User user) {
		SqlRowSet rs = this.jdbcTemplate.queryForRowSet(SQL_GET_BY_USER, user.getId());
		
		List<History> histories = new ArrayList<>();
		while(rs.next()) {
			histories.add(convertToObject(rs));
		}
		
		return histories;
	}
}
