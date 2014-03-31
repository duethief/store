package com.bookstore.dao.sqlExecutor;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.UserDao;
import com.bookstore.entities.User;

@Repository
public class UserDaoImplWithSqlExecutor extends CommDaoImplWithSqlExecutor<User> implements UserDao {
	public UserDaoImplWithSqlExecutor() {
		super(User.class);
	}
}
