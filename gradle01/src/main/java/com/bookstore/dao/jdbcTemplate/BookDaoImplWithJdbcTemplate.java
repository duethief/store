package com.bookstore.dao.jdbcTemplate;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.BookDao;
import com.bookstore.entities.Book;

@Repository
public class BookDaoImplWithJdbcTemplate extends CommDaoImplWithJdbcTemplate<Book> implements BookDao {
	public BookDaoImplWithJdbcTemplate() {
		super(Book.class);
	}
}
