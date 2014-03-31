package com.bookstore.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
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
import com.bookstore.domain.HistoryActionType;
import com.bookstore.domain.UserLevel;
import com.bookstore.entities.Book;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/hibernateContext.xml")
public class HistoryDaoTest {
	@Autowired
	private ApplicationContext context;
	
	private BookDao bookDao;
	
	private UserDao userDao;
	
	private HistoryDao historyDao;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private List<Book> getBooks() {
		List<Book> books = new ArrayList<Book>();
		
		for(int i = 0; i < 3; i++) {
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
		
		for(int i = 0; i < 3; i++) {
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
	
	@Test
	public void update() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
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
	
//	@Test
//	public void searchByUserId() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		List<History> searchedHistorys = historyDao.searchByUserId(1);
//		assertThat(searchedHistorys.size(), is(2));
//		
//		searchedHistorys = historyDao.searchByUserId(2);
//		assertThat(searchedHistorys.size(), is(1));
//	}
//	
//	@Test
//	public void searchByBookId() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		List<History> searchedHistorys = historyDao.searchByBookId(1);
//		assertThat(searchedHistorys.size(), is(1));
//		
//		searchedHistorys = historyDao.searchByBookId(2);
//		assertThat(searchedHistorys.size(), is(2));
//	}
	
//	@Test
//	public void delete() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		expectedException.expect(SQLException.class);
//		expectedException.expectMessage("Illegal operation on empty result set.");
//		
//		List<History> histories = getHistorys();
//		for(History history : histories) {
//			historyDao.delete(history);
//			historyDao.getById(history.getId());
//		}
//	}
}
