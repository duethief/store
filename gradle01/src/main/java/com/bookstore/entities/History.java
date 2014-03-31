package com.bookstore.entities;

import java.util.Date;

import javax.persistence.*;

import com.bookstore.domain.HistoryActionType;

@Entity
@Table(name="histories")
public class History {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	@ManyToOne
	@JoinColumn(name = "bookId")
	private Book book;
	@Enumerated
	@Column(name = "actionType")
	private HistoryActionType actionType;
	@Column(name = "insertDate")
	private Date insertDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public HistoryActionType getActionType() {
		return actionType;
	}

	public void setActionType(HistoryActionType actionType) {
		this.actionType = actionType;
	}

	public Date getInsertDate() {
		return insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = new Date(Math.round(insertDate.getTime() / 1000d) * 1000);
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + user.getId() + ", bookId=" + book.getId() + ", actionType=" + actionType + ", insertDate=" + insertDate + "]";
	}
}
