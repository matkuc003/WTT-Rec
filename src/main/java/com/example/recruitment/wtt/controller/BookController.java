package com.example.recruitment.wtt.controller;

import com.example.recruitment.wtt.model.Book;
import com.example.recruitment.wtt.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CacheConfig(cacheNames = "book")
public class BookController {
    @Autowired
    BookService bookService;

    @GetMapping("/book")
    public ResponseEntity<Book> getBookByISBN(@RequestParam String isbn){
        return bookService.getBookByISBN(isbn);
    }
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getBooksByCategory(@RequestParam String category){
        return bookService.getBooksByCategory(category);
    }
    @GetMapping("/authors")
    public ResponseEntity<Book> getAuthorsRating(){
        return bookService.getAuthorsRating();
    }
    @GetMapping("/bookWithNoPage")
    public ResponseEntity<Book> getBookWithPageCountGreaterThan(@RequestParam Integer pageCount){
        return bookService.getBookWithPageCountGreaterThan(pageCount);
    }
    @GetMapping("/theBestBooks")
    public ResponseEntity<List<Book>> getBookWithPageCountGreaterThan(@RequestParam Integer pagePerHour, @RequestParam Double avgHour){
        return bookService.getTheBestBooks(pagePerHour,avgHour);
    }
    @GetMapping("/viewedBooks")
    public ResponseEntity<List<Book>> getRecentlyViewedBooks(){
        return bookService.getRecentlyViewedBooks();
    }
}
