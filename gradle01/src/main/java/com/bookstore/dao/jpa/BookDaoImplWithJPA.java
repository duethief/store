package com.bookstore.dao.jpa;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.BookDao;
import com.bookstore.entities.Book;

@Repository
public class BookDaoImplWithJPA extends CommDaoImplWithJPA<Book> implements BookDao {
	public BookDaoImplWithJPA() {
		super(Book.class);
	}
}
