package com.bookstore.domain

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException

import com.bookstore.entities.Book

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext
import org.springframework.test.context.ContextConfiguration;

import spock.lang.*

@ContextConfiguration("/appContext.xml")
class BookDaoTest2 extends Specification {
	static final String PREFIX_BOOK_NAME = "book name"
    static final String PREFIX_BOOK_AUTHOR = "author name"
    static final String PREFIX_COMMENT = "comment"
	
	@Autowired
	ApplicationContext context
	
	def bookDao
	
	def "getBooks"() {
		def book1 = new Book()
		book1.id = 1
		book1.name = "book name1"
		book1.author = "author name1"
		book1.comment = "comment1"
		book1.publishDate = new Date()
		book1.status = BookStatus.CanRent
		
		def book2 = new Book()
		book2.id = 2
		book2.name = "book name2"
		book2.author = "author name2"
		book2.comment = "comment2"
		book2.publishDate = new Date()
		book2.status = BookStatus.CanRent
		
		def book3 = new Book()
		book3.id = 3
		book3.name = "book name3"
		book3.author = "author name3"
		book3.comment = "comment3"
		book3.publishDate = new Date()
		book3.status = BookStatus.CanRent
		
		def books = Arrays.asList(book1, book2, book3)
		return books
	}
	
	def "compareBook"(book) {
		def dbBook = bookDao.getById(book.getId())
		
		assert dbBook.getName() == book.getName()
		assert dbBook.getAuthor() == book.getAuthor()
		assert dbBook.getComment() == book.getComment()
		assert dbBook.getPublishDate().toString() == book.getPublishDate().toString()
		assert dbBook.getStatus() == book.getStatus()
		
		return true
	}
	
	def setup() {
		bookDao = context.getBean("bookDao")
		
		bookDao.deleteAll()
		assert bookDao.countAll() == 0
		
		def books = getBooks()
		for(book in books) {
			bookDao.add(book)
		}
		assert bookDao.countAll() == books.size()
	}
	
	def "update"() {
		when:
		List<Book> books = getBooks();
		
		then:
		books.every() {
			it.setName("changed name")
			it.setPublishDate(new Date())
			it.setAuthor("changed author")
			bookDao.update(it);
			
			compareBook(it)
		}
	}
	
	def "getAll"() {
		expect:
		bookDao.getAll().size() == getBooks().size()
	}
	
//	def "search"() {
//		expect:
//		def searchedBooks = bookDao.search(id);
//		searchedBooks.size() == cnt
//		
//		where:
//		id|cnt
//		"1"|1
//		"2"|1
//		"3"|1
//		"name"|3
//	}
	
//	def "delete"() {
//		when:
//		def books = getBooks();
//		for(book in books) {
//			bookDao.delete(book)
//			bookDao.get(book.getId())
//		}
//		then:
//		def e = thrown(SQLException)
//		e.message == "Illegal operation on empty result set."
//	}
}
