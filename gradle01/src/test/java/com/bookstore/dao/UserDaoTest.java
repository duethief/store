package com.bookstore.dao;

import java.sql.SQLException;
import java.util.Arrays;
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

import com.bookstore.domain.UserLevel;
import com.bookstore.entities.User;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/sqlExecutorContext.xml")
public class UserDaoTest {
	@Autowired
	private ApplicationContext context;
	
	private UserDao userDao;
	private HistoryDao historyDao;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private List<User> getUsers() {
		User user1 = new User();
		user1.setId(1);
		user1.setName("name1");
		user1.setPassword("password1");
		user1.setPoint(0);
		user1.setLevel(UserLevel.NORMAL);
		
		User user2 = new User();
		user2.setId(2);
		user2.setName("name2");
		user2.setPassword("password2");
		user2.setPoint(0);
		user2.setLevel(UserLevel.NORMAL);
		
		User user3 = new User();
		user3.setId(3);
		user3.setName("name3");
		user3.setPassword("password3");
		user3.setPoint(0);
		user3.setLevel(UserLevel.NORMAL);
		
		List<User> users = Arrays.asList(user1, user2, user3);
		return users;
	}
	
	private void compareUser(User user) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		User dbUser = userDao.getById(user.getId());
		
		assertThat(dbUser.getName(), is(user.getName()));
		assertThat(dbUser.getPassword(), is(user.getPassword()));
		assertThat(dbUser.getPoint(), is(user.getPoint()));
		assertThat(dbUser.getLevel(), is(user.getLevel()));
	}
	
	@Before
	public void setUp() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		historyDao = context.getBean(HistoryDao.class);
		
        historyDao.deleteAll();
        assertThat(historyDao.countAll(), is(0));
        
		userDao = context.getBean(UserDao.class);
		
		userDao.deleteAll();
		assertThat(userDao.countAll(), is(0));
		
		List<User> users = getUsers();
		for(User user : users) {
			userDao.add(user);
		}
		assertThat(userDao.countAll(), is(users.size()));
	}
	
	@Test
	public void update() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		List<User> users = getUsers();
		for(User user : users) {
			user.setName("changed name");
			user.setPassword("changed pass");
			user.setPoint(1);
			user.setLevel(UserLevel.READER);
			userDao.update(user);
			
			compareUser(user);
		}
	}
	
	@Test
	public void getAll() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		assertThat(userDao.getAll().size(), is(getUsers().size()));
	}
	
//	@Test
//	public void search() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		List<User> searchedUsers = userDao.search("1");
//		assertThat(searchedUsers.size(), is(1));
//		
//		searchedUsers = userDao.search("2");
//		assertThat(searchedUsers.size(), is(1));
//		
//		searchedUsers = userDao.search("3");
//		assertThat(searchedUsers.size(), is(1));
//		
//		searchedUsers = userDao.search("name");
//		assertThat(searchedUsers.size(), is(3));
//	}
	
//	@Test
//	public void delete() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
//		expectedException.expect(SQLException.class);
//		expectedException.expectMessage("Illegal operation on empty result set.");
//		
//		List<User> users = getUsers();
//		for(User user : users) {
//			userDao.delete(user);
//			userDao.getById(user.getId());
//		}
//	}
}
