package com.bookstore.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.bookstore.domain.BookStatus;

@Entity
@Table(name = "books")
public class Book {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(name = "name", length = 50)
	private String name;
	@Column(name = "author", length = 50)
	private String author;
	@Column(name = "publishDate")
	private Date publishDate;
	@Column(name = "comment", length = 255)
	private String comment;
	@Column(name = "status")
	@Enumerated(EnumType.ORDINAL)
	private BookStatus status;
	@ManyToOne
	@JoinColumn(name = "rentUserId", nullable = true)
	private User rentUser;
	@OneToMany(mappedBy = "book", cascade = {CascadeType.ALL})
	private Set<History> historeis = new HashSet<History>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = new Date(Math.round(publishDate.getTime() / 1000d) * 1000);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public BookStatus getStatus() {
		return status;
	}

	public void setStatus(BookStatus status) {
		this.status = status;
	}

	public User getRentUser() {
		return rentUser;
	}

	public void setRentUser(User rentUser) {
		this.rentUser = rentUser;
	}
	
	@Override
	public String toString() {
		return "Book [id=" + id + ", name=" + name + ", author=" + author + ", publishDate=" + publishDate + ", comment=" + comment + "]";
	}
}
