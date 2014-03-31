package com.bookstore.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bookstore.domain.BookStatus;
import com.bookstore.entities.Book;
import com.bookstore.entities.User;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbcTemplateContext.xml")
public class BookDaoTest {
	@Autowired
	private ApplicationContext context;
	
	private BookDao bookDao;
	private HistoryDao historyDao;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private List<Book> getBooks() {
		Book book1 = new Book();
		book1.setId(1);
		book1.setName("book name01");
		book1.setAuthor("author name 01");
		book1.setComment("comment01");
		book1.setPublishDate(new Date());
		book1.setStatus(BookStatus.CanRent);
		
		Book book2 = new Book();
		book2.setId(2);
		book2.setName("book name02");
		book2.setAuthor("author name 02");
		book2.setComment("comment02");
		book2.setPublishDate(new Date());
		book2.setStatus(BookStatus.CanRent);
		
		Book book3 = new Book();
		book3.setId(3);
		book3.setName("book name03");
		book3.setAuthor("author name 03");
		book3.setComment("comment03");
		book3.setPublishDate(new Date());
		book3.setStatus(BookStatus.CanRent);
		
		List<Book> books = Arrays.asList(book1, book2, book3);
		return books;
	}
	
	private void compareBook(Book book) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Book dbBook = bookDao.getById(book.getId());
		
		assertThat(dbBook.getName(), is(book.getName()));
		assertThat(dbBook.getAuthor(), is(book.getAuthor()));
		assertThat(dbBook.getComment(), is(book.getComment()));
		assertThat(dbBook.getPublishDate().toString(), is(book.getPublishDate().toString()));
		assertThat(dbBook.getRentUser().getId(), is(book.getRentUser().getId()));
	}
	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		historyDao = context.getBean(HistoryDao.class);
		
        historyDao.deleteAll();
        assertThat(historyDao.countAll(), is(0));
        
		bookDao = context.getBean(BookDao.class);
		
		bookDao.deleteAll();
		assertThat(bookDao.countAll(), is(0));
		
		List<Book> books = getBooks();
		for(Book book : books) {
			bookDao.add(book);
		}
		assertThat(bookDao.countAll(), is(books.size()));
	}
	
	@Test
	public void update() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Book> books = getBooks();
		for(Book book : books) {
			book.setName("changed name");
			book.setPublishDate(new Date());
			book.setAuthor("changed author");
			book.setStatus(BookStatus.RentNow);
			User user = new User();
			user.setId(1);
			book.setRentUser(user);
			bookDao.update(book);
			
			compareBook(book);
		}
	}
	
	@Test
	public void getAll() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		assertThat(bookDao.getAll().size(), is(getBooks().size()));
	}
	
//	@Test
//	public void search() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		List<Book> searchedBooks = bookDao.search("01");
//		assertThat(searchedBooks.size(), is(1));
//		
//		searchedBooks = bookDao.search("02");
//		assertThat(searchedBooks.size(), is(1));
//		
//		searchedBooks = bookDao.search("03");
//		assertThat(searchedBooks.size(), is(1));
//		
//		searchedBooks = bookDao.search("name");
//		assertThat(searchedBooks.size(), is(3));
//	}
	
//	@Test
//	public void delete() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		expectedException.expect(SQLException.class);
//		expectedException.expectMessage("Illegal operation on empty result set.");
//		
//		List<Book> books = getBooks();
//		for(Book book : books) {
//			bookDao.delete(book);
//			bookDao.getById(book.getId());
//		}
//	}
}
