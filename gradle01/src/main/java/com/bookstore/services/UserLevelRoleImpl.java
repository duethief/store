package com.bookstore.services;

import com.bookstore.domain.UserLevel;
import com.bookstore.entities.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserLevelRoleImpl implements UserLevelRole {
	private int addRentPoint;
	private int readerThreshold;
	private int mvpThreshold;
	
	@Override
	public int getAddRentPoint() {
		return addRentPoint;
	}

	@Override
	public void setAddRentPoint(int addRentPoint) {
		this.addRentPoint = addRentPoint;
	}

	@Override
	public int getReaderThreshold() {
		return readerThreshold;
	}

	@Override
	public void setReaderThreshold(int readerThreshold) {
		this.readerThreshold = readerThreshold;
	}

	@Override
	public int getMvpThreshold() {
		return mvpThreshold;
	}

	@Override
	public void setMvpThreshold(int mvpThreshold) {
		this.mvpThreshold = mvpThreshold;
	}
	
	@Override
	public void updatePointAndLevel(User user) {
		user.setPoint(user.getPoint() + addRentPoint);
		if (user.getPoint() >= mvpThreshold) {
			user.setLevel(UserLevel.MVP);
		} else if (user.getPoint() >= readerThreshold) {
			user.setLevel(UserLevel.READER);
		} else {
			user.setLevel(UserLevel.NORMAL);
		}
	}
}
