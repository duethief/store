package com.bookstore.dao.springData;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.bookstore.dao.UserDao;
import com.bookstore.entities.User;

@Repository
public class UserDaoImplWithSpringData extends CommDaoImplWithSpringData<User> implements UserDao {

}

interface UserRepository extends JpaRepository<User, Serializable>, QueryDslPredicateExecutor<User> {

}
