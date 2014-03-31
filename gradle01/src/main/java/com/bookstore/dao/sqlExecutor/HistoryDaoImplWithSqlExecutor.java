package com.bookstore.dao.sqlExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.HistoryDao;
import com.bookstore.dao.sqlExecutor.SqlExecutor.ExecuteSelectQuery;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

@Repository
public class HistoryDaoImplWithSqlExecutor extends CommDaoImplWithSqlExecutor<History> implements HistoryDao {
	private final String SQL_GET_BY_USER = "select id, userId, bookId, actionType, insertDate from Histories where userId = ?";
	
	public HistoryDaoImplWithSqlExecutor() {
		super(History.class);
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<History> getByUser(final User user) {
		return (List<History>) this.sqlExecutor.execute(new ExecuteSelectQuery() {
			@Override
			public PreparedStatement getPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement st = conn.prepareStatement(SQL_GET_BY_USER);
				st.setInt(1, user.getId());
				return st;
			}
			
			@Override
			public Object parseResultSet(ResultSet rs) throws SQLException {
				List<History> objs = new ArrayList<History>();
				while(rs.next()) {
					objs.add(convertToObject(rs));
				}
				
				return objs;
			}
		});
	}
}
