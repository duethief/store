package com.bookstore.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.dao.BookDao;
import com.bookstore.entities.Book;

@Service
public class BookServiceImpl implements BookService {
	@Autowired
	BookDao bookDao;
	
	@Override
	public List<Book> listup() {
		List<Book> books = bookDao.getAll();
		
		Collections.sort(books, new Comparator<Book>() {
			@Override
			public int compare(Book o1, Book o2) {
				return Integer.compare(o1.getStatus().intValue(), o2.getStatus().intValue());
			}
		});
		
		return books;
	}
}
