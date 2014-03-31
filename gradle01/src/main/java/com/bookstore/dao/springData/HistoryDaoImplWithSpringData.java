package com.bookstore.dao.springData;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.bookstore.dao.HistoryDao;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

@Repository
public class HistoryDaoImplWithSpringData extends CommDaoImplWithSpringData<History> implements HistoryDao {
	@Override
	public List<History> getByUser(User user) {
		return ((HistoryRepository) repository).findByUser(user);
	}
}

interface HistoryRepository extends JpaRepository<History, Serializable>, QueryDslPredicateExecutor<History> {
	List<History> findByUser(User user);
}
