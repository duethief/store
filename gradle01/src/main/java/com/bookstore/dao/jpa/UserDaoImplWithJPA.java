package com.bookstore.dao.jpa;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.UserDao;
import com.bookstore.entities.User;

@Repository
public class UserDaoImplWithJPA extends CommDaoImplWithJPA<User> implements UserDao {
	public UserDaoImplWithJPA() {
		super(User.class);
	}
}
