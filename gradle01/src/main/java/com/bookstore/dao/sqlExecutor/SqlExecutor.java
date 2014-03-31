package com.bookstore.dao.sqlExecutor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bookstore.dao.ConnectionFactory;

@Component
public class SqlExecutor {
	@Autowired
	private ConnectionFactory connectionFactory;
	
	public ConnectionFactory getConnectionFactory() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		return connectionFactory;
	}
	
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	interface ExecuteUpdateQuery {
		PreparedStatement getPreparedStatement(Connection conn) throws SQLException;
	}
	
	interface ExecuteSelectQuery {
		PreparedStatement getPreparedStatement(Connection conn) throws SQLException;
		Object parseResultSet(ResultSet rs) throws SQLException;
	}
	
	public void execute(ExecuteUpdateQuery query) {
		Connection conn = connectionFactory.getConnection();
		PreparedStatement st = null;
		
		try {
			st = query.getPreparedStatement(conn);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			if(st != null) {
				try {
					st.close();
				} catch(Exception ex) {}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(Exception ex) {}
			}
		}
	}
	
	public Object execute(ExecuteSelectQuery query) {
		Connection conn = connectionFactory.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = query.getPreparedStatement(conn);
			rs = st.executeQuery();
			return query.parseResultSet(rs);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		finally {
			if(rs != null) {
				try {
					rs.close();
				} catch(Exception ex) {}
			}
			if(st != null) {
				try {
					st.close();
				} catch(Exception ex) {}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(Exception ex) {}
			}
		}
	}
}
