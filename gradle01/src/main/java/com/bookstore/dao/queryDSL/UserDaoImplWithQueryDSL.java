package com.bookstore.dao.queryDSL;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.UserDao;
import com.bookstore.entities.User;

@Repository
public class UserDaoImplWithQueryDSL extends CommDaoImplWithQueryDSL<User> implements UserDao {
	public UserDaoImplWithQueryDSL() {
		super(User.class);
	}
}
