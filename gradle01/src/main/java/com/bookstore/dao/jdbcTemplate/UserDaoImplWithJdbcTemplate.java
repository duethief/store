package com.bookstore.dao.jdbcTemplate;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.UserDao;
import com.bookstore.entities.User;

@Repository
public class UserDaoImplWithJdbcTemplate extends CommDaoImplWithJdbcTemplate<User> implements UserDao {
	public UserDaoImplWithJdbcTemplate() {
		super(User.class);
	}
}
