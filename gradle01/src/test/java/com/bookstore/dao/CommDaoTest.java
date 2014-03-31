package com.bookstore.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.domain.BookStatus;
import com.bookstore.domain.HistoryActionType;
import com.bookstore.domain.UserLevel;
import com.bookstore.entities.Book;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/springDataContext.xml")
@Transactional
public class CommDaoTest {
	@Autowired
	private ApplicationContext context;
	
	private CommDao<Book> bookDao;
	private UserDao userDao;
	private HistoryDao historyDao;
	
	
	private List<Book> getBooks() {
		List<Book> books = new ArrayList<Book>();
		
		for(int i = 0; i < 1; i++) {
			Book book = new Book();
			book.setName("book name" + i);
			book.setAuthor("author name" + i);
			book.setComment("comment" + i);
			book.setPublishDate(new Date());
			book.setStatus(BookStatus.CanRent);
			books.add(book);
		}
		
		return books;
	}
	
	private List<User> getUsers() {
		List<User> users = new ArrayList<User>();
		
		for(int i = 0; i < 1; i++) {
			User user = new User();
			user.setName("name" + i);
			user.setPassword("password" + i);
			user.setPoint(0);
			user.setLevel(UserLevel.NORMAL);
			users.add(user);
		}
		
		return users;
	}
	
	private List<History> getHistories(List<User> users, List<Book> books) {
		List<History> histories = new ArrayList<History>();
		
		for(int i = 0; i < users.size(); i++ ) {
			History history = new History();
			history.setUser(users.get(i));
			history.setBook(books.get(i));
			history.setActionType(HistoryActionType.RENT);
			history.setInsertDate(new Date());
			histories.add(history);
		}
		
		return histories;
	}
	
	private void compareBook(Book book) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		Book dbBook = bookDao.getById(book.getId());
		
		assertThat(dbBook.getName(), is(book.getName()));
		assertThat(dbBook.getAuthor(), is(book.getAuthor()));
		assertThat(dbBook.getComment(), is(book.getComment()));
		assertThat(dbBook.getPublishDate().getTime(), is(book.getPublishDate().getTime()));
	}
	
	private void compareUser(User user) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		User dbUser = userDao.getById(user.getId());
		
		assertThat(dbUser.getName(), is(user.getName()));
		assertThat(dbUser.getPassword(), is(user.getPassword()));
		assertThat(dbUser.getPoint(), is(user.getPoint()));
		assertThat(dbUser.getLevel(), is(user.getLevel()));
	}
	
	private void compareHistory(History history) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		History dbHistory = historyDao.getById(history.getId());
		
		assertThat(dbHistory.getUser().getId(), is(history.getUser().getId()));
		assertThat(dbHistory.getBook().getId(), is(history.getBook().getId()));
		assertThat(dbHistory.getActionType(), is(history.getActionType()));
		assertThat(dbHistory.getInsertDate().getTime(), is(history.getInsertDate().getTime()));
	}
	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		bookDao = context.getBean(BookDao.class);
		userDao = context.getBean(UserDao.class);
		historyDao = context.getBean(HistoryDao.class);
		
		historyDao.deleteAll();
		assertThat(historyDao.countAll(), is(0));
		
		bookDao.deleteAll();
		assertThat(bookDao.countAll(), is(0));
		
		List<Book> books = getBooks();
		for(Book book : books) {
			bookDao.add(book);
		}
		assertThat(bookDao.countAll(), is(books.size()));
		
		userDao.deleteAll();
		assertThat(userDao.countAll(), is(0));
		
		List<User> users = getUsers();
		for(User user : users) {
			userDao.add(user);
		}
		assertThat(userDao.countAll(), is(users.size()));
		
		List<History> histories = getHistories(users, books);
		for(History history : histories) {
			historyDao.add(history);
		}
		assertThat(historyDao.countAll(), is(histories.size()));
	}
	
//	@Test
//	public void getById() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		User user1 = userDao.getById(2);
//		User user2 = userDao2.getById(2);
//		
//		assertThat(user1.getName(), is(user2.getName()));
//		assertThat(user1.getPassword(), is(user2.getPassword()));
//		assertThat(user1.getPoint(), is(user2.getPoint()));
//		assertThat(user1.getLevel(), is(user2.getLevel()));
//	}
	
	@Test
	public void update() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Book> books = bookDao.getAll();
		for(Book book : books) {
			book.setName("changed name");
			book.setPublishDate(new Date());
			book.setAuthor("changed author");
			bookDao.update(book);
			
			compareBook(book);
		}
		
		List<User> users = userDao.getAll();
		for(User user : users) {
			user.setName("changed name");
			user.setPassword("changed pass");
			user.setPoint(1);
			user.setLevel(UserLevel.READER);
			userDao.update(user);
			
			compareUser(user);
		}
		
		User newUser = getUsers().get(0);
		userDao.add(newUser);
		
		Book newBook = getBooks().get(0);
		bookDao.add(newBook);
		
		List<History> histories = historyDao.getAll();
		for(History history : histories) {
			history.setUser(newUser);
			history.setBook(newBook);
			history.setActionType(HistoryActionType.RETURN);
			history.setInsertDate(new Date());
			historyDao.update(history);
			
			compareHistory(history);
		}
	}
	
	@Test
	public void getAll() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<Book> books = getBooks();
		assertThat(bookDao.getAll().size(), is(books.size()));
		
		List<User> users = getUsers();
		assertThat(userDao.getAll().size(), is(users.size()));
		
		assertThat(historyDao.getAll().size(), is(getHistories(users, books).size()));
	}
}
