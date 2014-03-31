package com.bookstore.dao.queryDSL;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.BookDao;
import com.bookstore.entities.Book;

@Repository
public class BookDaoImplWithQueryDSL extends CommDaoImplWithQueryDSL<Book> implements BookDao {
	public BookDaoImplWithQueryDSL() {
		super(Book.class);
	}
}
