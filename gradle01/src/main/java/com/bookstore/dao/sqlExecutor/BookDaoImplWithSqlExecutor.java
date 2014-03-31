package com.bookstore.dao.sqlExecutor;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.BookDao;
import com.bookstore.entities.Book;

@Repository
public class BookDaoImplWithSqlExecutor extends CommDaoImplWithSqlExecutor<Book> implements BookDao {
	public BookDaoImplWithSqlExecutor() {
		super(Book.class);
	}
}
