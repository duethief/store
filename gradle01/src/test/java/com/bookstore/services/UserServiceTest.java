package com.bookstore.services;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bookstore.dao.BookDao;
import com.bookstore.dao.HistoryDao;
import com.bookstore.dao.UserDao;
import com.bookstore.domain.BookStatus;
import com.bookstore.domain.HistoryActionType;
import com.bookstore.domain.UserLevel;
import com.bookstore.entities.Book;
import com.bookstore.entities.History;
import com.bookstore.entities.User;
import com.bookstore.services.UserLevelRole;
import com.bookstore.services.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/jdbcTemplateContext.xml")
public class UserServiceTest {
	@Autowired
	private ApplicationContext context;
	
	UserDao userDao;
	BookDao bookDao;
	HistoryDao historyDao;
	UserService userService;
	UserLevelRole userLevelRole;
	
	private User setUser(int point, UserLevel level) {
		User user = new User();
		user.setId(1);
		user.setName("name1");
		user.setPassword("password1");
		user.setPoint(point);
		user.setLevel(level);
		userDao.add(user);
		return user;
	}
	
	private Book getBook() {
		Book book = new Book();
		book.setId(1);
		book.setName("book name01");
		book.setAuthor("author name 01");
		book.setComment("comment01");
		book.setPublishDate(new Date());
		book.setStatus(BookStatus.CanRent);
		book.setRentUser(null);
		return book;
	}
	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		historyDao = context.getBean(HistoryDao.class);
		historyDao.deleteAll();
        
		userDao = context.getBean(UserDao.class);
		userDao.deleteAll();
		
		bookDao = context.getBean(BookDao.class);
		bookDao.deleteAll();
		bookDao.add(getBook());
		
		userService = context.getBean(UserService.class);
		userLevelRole = context.getBean(UserLevelRole.class);
	}
	
	//@Test
	public void rent() {
		User user = setUser(0, UserLevel.NORMAL);
		userService.rent(user, getBook());
		assertThat(userDao.getById(user.getId()).getPoint(), is(user.getPoint() + userLevelRole.getAddRentPoint()));
		
		assertThat(bookDao.getById(getBook().getId()).getStatus(), is(BookStatus.RentNow));
		assertThat(bookDao.getById(getBook().getId()).getRentUser().getId(), is(user.getId()));
		
		History history = historyDao.getByUser(user).get(0);
		assertThat(history.getActionType(), is(HistoryActionType.RENT));
	}
	
	//@Test
	public void setUserLevel() {
		testLevelup(90, UserLevel.NORMAL, UserLevel.READER);
		testLevelup(290, UserLevel.READER, UserLevel.MVP);
		
		testLevelup(0, UserLevel.NORMAL, UserLevel.NORMAL);
		testLevelup(100, UserLevel.READER, UserLevel.READER);
		testLevelup(300, UserLevel.MVP, UserLevel.MVP);
	}
	
	private void testLevelup(int beforePoint, UserLevel beforeLevel, UserLevel afterLevel) {
		User user = setUser(beforePoint, beforeLevel);
		userService.rent(user, getBook());
		assertThat(userDao.getById(user.getId()).getLevel(), is(afterLevel));
		
		userService.returnBook(user, getBook());
		historyDao.deleteAll();
		userDao.deleteAll();
	}
	
	public void returnBook() {
		User user = setUser(0, UserLevel.NORMAL);
		userService.returnBook(user, getBook());
		assertThat(bookDao.getById(getBook().getId()).getStatus(), is(BookStatus.CanRent));
		assertThat(bookDao.getById(getBook().getId()).getRentUser(), is(nullValue()));
		
		History history = historyDao.getByUser(user).get(0);
		assertThat(history.getActionType(), is(HistoryActionType.RETURN));
	}
	
	//@Test
	public void getHistories() throws InterruptedException {
		User user = setUser(0, UserLevel.NORMAL);
		userService.rent(user, getBook());
		Thread.sleep(1000);
		userService.returnBook(user, getBook());
		
		List<History> histories = userService.listupHistories(user);
		assertThat(histories.get(0).getActionType(), is(HistoryActionType.RETURN));
		assertThat(histories.get(1).getActionType(), is(HistoryActionType.RENT));
		assertThat(histories.get(0).getInsertDate().getTime() > histories.get(1).getInsertDate().getTime(), is(true));
	}
	
	//@Transactional을 사용하면 userService가 proxy를 사용하기 때문에 형변환 불가
	//@Test(expected=NullPointerException.class)
	@DirtiesContext
	public void rentBookWithException() {
		((UserServiceImpl) userService).setUserLevelRole(null);
		User user = setUser(0, UserLevel.NORMAL);
		
		Book oldBook = bookDao.getById(getBook().getId());
		try {
			userService.rent(user, getBook());
		} finally {
			Book updatedBook = bookDao.getById(getBook().getId());
			assertThat(updatedBook.getStatus(), is(oldBook.getStatus()));
			assertThat(updatedBook.getRentUser(), is(nullValue()));
		}
	}
	
	@Test
	public void displayUserServiceObjectName() {
		System.out.println("UserService의 구현 객체는 " + userService.getClass().getName() + "입니다.");
		assertThat("userService는 UserServiceImpl이 할당되어 있지 않습니다.", userService instanceof UserServiceImpl, is(true));
	}
}
