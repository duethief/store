package com.bookstore.dao;

import java.util.List;

import com.bookstore.entities.History;
import com.bookstore.entities.User;

public interface HistoryDao extends CommDao<History> {
	public List<History> getByUser(User user);
}
