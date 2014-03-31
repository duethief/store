package com.bookstore.dao.springData;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.bookstore.dao.BookDao;
import com.bookstore.entities.Book;

@Repository
public class BookDaoImplWithSpringData extends CommDaoImplWithSpringData<Book> implements BookDao {

}

interface BookRepository extends JpaRepository<Book, Serializable>, QueryDslPredicateExecutor<Book> {

}
