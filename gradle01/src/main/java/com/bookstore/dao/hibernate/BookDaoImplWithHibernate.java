package com.bookstore.dao.hibernate;

import org.springframework.stereotype.Repository;

import com.bookstore.dao.BookDao;
import com.bookstore.entities.Book;

@Repository
public class BookDaoImplWithHibernate extends CommDaoImplWithHibernate<Book> implements BookDao {
	public BookDaoImplWithHibernate() {
		super(Book.class);
	}
}
