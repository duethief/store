package com.bookstore.controllers;

import com.bookstore.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by InSeok on 2014-03-31.
 */
@Controller
public class BookListController {
    @Autowired
    private BookService bookService;

    @RequestMapping(value = "books/list")
    public String getBookList(Model model) {
        model.addAttribute("books", bookService.listup());
        return "books";
    }
}
