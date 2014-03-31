package com.bookstore.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.bookstore.dao.BookDao;
import com.bookstore.dao.HistoryDao;
import com.bookstore.dao.UserDao;
import com.bookstore.domain.BookStatus;
import com.bookstore.domain.HistoryActionType;
import com.bookstore.domain.UserLevel;
import com.bookstore.entities.Book;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private BookDao bookDao;
	@Autowired
	private HistoryDao historyDao;
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private UserLevelRole userLevelRole;
	
	public void setUserLevelRole(UserLevelRole userLevelRole) {
		this.userLevelRole = userLevelRole;
	}
	
	@Override
	public void rent(User user, Book book) {
		book.setStatus(BookStatus.RentNow);
		book.setRentUser(user);
		bookDao.update(book);
		
		userLevelRole.updatePointAndLevel(user);
		userDao.update(user);
		
		History history = new History();
		history.setUser(user);
		history.setBook(book);
		history.setActionType(HistoryActionType.RENT);
		history.setInsertDate(new Date());
		historyDao.add(history);
	}
	
	@Override
	public void returnBook(User user, Book book) {
		book.setStatus(BookStatus.CanRent);
		book.setRentUser(null);
		bookDao.update(book);
		
		History history = new History();
		history.setUser(user);
		history.setBook(book);
		history.setActionType(HistoryActionType.RETURN);
		history.setInsertDate(new Date());
		historyDao.add(history);
	}
	
	@Override
	public List<User> listup() {
		return null;
	}

	@Override
	public List<History> listupHistories(User user) {
		List<History> histories = historyDao.getByUser(user);
		
		Collections.sort(histories, new Comparator<History>() {
			@Override
			public int compare(History arg0, History arg1) {
				return Long.compare(arg1.getInsertDate().getTime(), arg0.getInsertDate().getTime());
			}			
		});
		
		return histories;
	}
}
