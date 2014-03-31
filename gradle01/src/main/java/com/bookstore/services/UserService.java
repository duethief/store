package com.bookstore.services;

import java.util.List;

import com.bookstore.entities.Book;
import com.bookstore.entities.History;
import com.bookstore.entities.User;

public interface UserService {
	public void rent(User user, Book book);
	public void returnBook(User user, Book book);
	List<User> listup();
	List<History> listupHistories(User user);
}
