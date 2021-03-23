package com.example.recruitment.wtt;

import com.example.recruitment.wtt.model.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class WttApplicationTests {
	@Autowired
	WebApplicationContext webApplicationContext;
	MockMvc mvc;
	@BeforeEach
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(obj);
	}
	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}

	@Test
	@Order(1)
	public void getBooksByCategoryTest() throws Exception {
		String uri = "/api/books";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("category","Computers")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Book[] bookList = mapFromJson(content, Book[].class);
		assertTrue(bookList.length > 0);
	}
	@Test
	@Order(2)
	public void getBookByISBNTest() throws Exception {
		String uri = "/api/book";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("isbn","9789793780146")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Book book = mapFromJson(content, Book.class);
		assertTrue(book.getIsbn().equals("9789793780146"));
	}
	@Test
	@Order(3)
	public void getBooksWithPageCountGreaterThanTest() throws Exception {
		String uri = "/api/bookWithNoPage";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("pageCount","300")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Book book = mapFromJson(content, Book.class);
		assertTrue(book.getPageCount()>300);
	}
	@Test
	@Order(4)
	public void getTheBestBooks() throws Exception {
		String uri = "/api/theBestBooks";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).param("pagePerHour","10").param("avgHour","3")
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		Integer pagesPerMonth = 10*3*30;
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Book[] books = mapFromJson(content, Book[].class);
		List<Book> bookList = Arrays.asList(books);
		bookList.forEach(boo->assertTrue(boo.getPageCount()<pagesPerMonth));
	}
	@Test
	@Order(5)
	public void getAuthorsRating() throws Exception {
		String uri = "/api/authors";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertTrue(!content.isEmpty());
	}

}
