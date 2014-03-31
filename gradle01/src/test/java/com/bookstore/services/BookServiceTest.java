package com.bookstore.services;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bookstore.dao.BookDao;
import com.bookstore.dao.HistoryDao;
import com.bookstore.dao.UserDao;
import com.bookstore.domain.BookStatus;
import com.bookstore.domain.UserLevel;
import com.bookstore.entities.Book;
import com.bookstore.entities.User;
import com.bookstore.services.BookService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbcTemplateContext.xml")
public class BookServiceTest {
	@Autowired
	private ApplicationContext context;
	
	UserDao userDao;
	BookDao bookDao;
	HistoryDao historyDao;
	private BookService bookService;
	
	private User getUser() {
		User user = new User();
		user.setId(1);
		user.setName("name1");
		user.setPassword("password1");
		user.setPoint(0);
		user.setLevel(UserLevel.NORMAL);
		return user;
	}
	
	private List<Book> setBooks() {
		List<Book> books = new ArrayList<Book>();
		for (int i = 1; i < 50; i++) {
			Book book = new Book();
			book.setId(i);
			book.setName("book name");
			book.setAuthor("author name");
			book.setComment("comment");
			book.setPublishDate(new Date());
			book.setStatus(BookStatus.valueOf(i % BookStatus.values().length));
			book.setRentUser(book.getStatus() == BookStatus.RentNow ? getUser() : null);
			books.add(book);
			bookDao.add(book);
		}
		return books;
	}
	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		historyDao = context.getBean(HistoryDao.class);
		historyDao.deleteAll();
		
		userDao = context.getBean(UserDao.class);
		userDao.deleteAll();
		userDao.add(getUser());
		
		bookDao = context.getBean(BookDao.class);
		bookDao.deleteAll();
		
		bookService = context.getBean(BookService.class);
	}
	
	@Test
	public void listup() {
		setBooks();
		
		List<Book> books = bookService.listup();
		int lastStatus = 0;
		for (Book book : books) {
			assertThat(lastStatus <= book.getStatus().intValue(), is(true));
			lastStatus = book.getStatus().intValue();
		}
	}
}
