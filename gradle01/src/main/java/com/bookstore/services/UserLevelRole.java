package com.bookstore.services;

import com.bookstore.entities.User;

public interface UserLevelRole {
	int getAddRentPoint();
	void setAddRentPoint(int addRentPoint);
	int getReaderThreshold();
	void setReaderThreshold(int readerThreshold);
	int getMvpThreshold();
	void setMvpThreshold(int mvpThreshold);
	void updatePointAndLevel(User user);
}
