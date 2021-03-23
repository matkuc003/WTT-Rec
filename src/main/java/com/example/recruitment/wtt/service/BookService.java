package com.example.recruitment.wtt.service;

import com.example.recruitment.wtt.model.Book;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookService {
    ObjectMapper objectMapper = new ObjectMapper();
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("books.json").getFile());
    JsonNode jsonNode = objectMapper.readTree(file);
    Book[] books = objectMapper.treeToValue(jsonNode.get("items"), Book[].class);
    List<Book> bookList = new ArrayList(Arrays.asList(books));
    @Autowired
    private CacheManager cacheManager;

    public BookService() throws IOException {
    }

    @Cacheable(value = "book", key = "#isbn")
    public ResponseEntity<Book> getBookByISBN(String isbn) {
        try {
            Book book = bookList.stream().filter(boo -> boo.getIsbn().equals(isbn)).findFirst().get();
            return new ResponseEntity(book, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Book>> getBooksByCategory(String category) {
        try {
            List<Book> booksByCategory = bookList.stream().filter(boo -> boo.hasCategory(category)).collect(Collectors.toList());
            return new ResponseEntity(booksByCategory, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }
    public Set<String> getCategories() {
        try {
            Set<String> categories = bookList.stream().filter(book->book.getCategories()!=null).flatMap(book->book.getCategories().stream()).collect(Collectors.toSet());
            return categories;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ResponseEntity<Book> getBookWithPageCountGreaterThan(Integer pageCount) {
        try {
            Book book = bookList.stream().filter(boo -> boo.pageCountGreaterThan(pageCount)).findFirst().get();
            return new ResponseEntity(book, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Book>> getTheBestBooks(Integer pagePerHour, Double avgHours) {
        try {
            Double maxPagesPerMonth = (pagePerHour * avgHours) * 30;
            List<Book> theBestBooksList = bookList.stream()
                    .filter(boo -> boo.pageCountLessThan(maxPagesPerMonth))
                    .filter(book -> book.avgRatingGreaterOREqual(4))
                    .collect(Collectors.toList());
            return new ResponseEntity(theBestBooksList, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<Book>> getRecentlyViewedBooks() {
        try {
            return new ResponseEntity(cachedValues("book"), HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Book> getAuthorsRating() {
        try {
            Map<String, Double> authorRatingMap = new HashMap<>();

            List authors = bookList.stream().flatMap(boo ->
            {
                if (boo.getAuthors() != null)
                    return boo.getAuthors().stream();
                else
                    return null;
            }).distinct().collect(Collectors.toList());

            authors.forEach(author -> bookList.stream()
                    .filter(bookWrittenBy -> bookWrittenBy
                            .hasAuthor(author.toString()))
                    .peek(book -> authorRatingMap.put(author.toString(), 0.0))
                    .filter(book -> book.getAverageRating() != null)
                    .mapToDouble(Book::getAverageRating)
                    .average()
                    .stream()
                    .forEach(rate -> {
                        authorRatingMap.put(author.toString(), rate);
                    }));
            Map<String, Double> result = authorRatingMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    Map<Object, Object> cachedValues(String cacheName) {
        CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(cacheName);
        com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache = caffeineCache.getNativeCache();
        Set<Object> keys = nativeCache.asMap().keySet();
        Map<Object, Object> values = nativeCache.getAllPresent(Arrays.asList(keys.toArray()));
        return values;
    }
}
