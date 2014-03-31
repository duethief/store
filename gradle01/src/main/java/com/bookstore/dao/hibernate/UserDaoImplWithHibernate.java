package com.bookstore.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.UserDao;
import com.bookstore.entities.User;

@Repository
public class UserDaoImplWithHibernate extends CommDaoImplWithHibernate<User> implements UserDao {
	public UserDaoImplWithHibernate() {
		super(User.class);
	}
}
