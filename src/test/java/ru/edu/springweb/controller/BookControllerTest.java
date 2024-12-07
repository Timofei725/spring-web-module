package ru.edu.springweb.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.edu.springweb.configuration.WebConfiguration;
import ru.edu.springweb.model.Book;
import ru.edu.springweb.service.BookService;
import ru.edu.springweb.service.SimpleBookService;


import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = WebConfiguration.class)
public class BookControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();
    @Autowired
    private BookService bookService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        bookService = new SimpleBookService();
        var bookController = new BookController(bookService);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @AfterEach
    void tearDown() {
        bookService.getAll()
                .forEach(existBook -> bookService.delete(existBook.getId()));
    }

    @Test
    void itShouldGetEmptyList() throws Exception {
        var response = mockMvc.perform(get("/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals("[]", response.getResponse().getContentAsString());
    }

    @Test
    void itShouldReturnBooks() throws Exception {
        var testBook = bookService.add(new Book("testTitle", "testAuthor"));
        var expectBook = OBJECT_MAPPER.writeValueAsString(testBook);

        var result = mockMvc.perform(get("/books"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertTrue(result.contains(expectBook));
    }

    @Test
    void itShouldAddBook() throws Exception {
        var testBook = new Book(getNextId(), "testTitle", "testAuthor");
        var expectBook = OBJECT_MAPPER.writeValueAsString(testBook);

        var result = mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(expectBook))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expectBook, result);
    }


    @Test
    void itShouldValidateTitle() throws Exception {
        var testBook = new Book(1L, "", "testAuthor");
        var expectBook = OBJECT_MAPPER.writeValueAsString(testBook);

        var result = mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(expectBook))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is5xxServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("title: Заголовок не может быть пустым", result);
    }

    @Test
    void itShouldValidateAuthor() throws Exception {
        var testBook = new Book(1L, "test", null);
        var expectBook = OBJECT_MAPPER.writeValueAsString(testBook);

        var result = mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                        .content(expectBook))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is5xxServerError())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("author: Автор не может отсутсовать", result);
    }

    @Test
    void itShouldUpdateBook() throws Exception {
        var testBook = new Book(getNextId(), "test", "testAuthor");
        bookService.add(testBook);
        testBook.setAuthor("updatedAuthor");
        var expectBook = OBJECT_MAPPER.writeValueAsString(testBook);

        var result = mockMvc.perform(put("/books/".concat(testBook.getId().toString())).contentType(MediaType.APPLICATION_JSON)
                        .content(expectBook))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expectBook, result);
    }

    @Test
    void itShouldUpdateNothing() throws Exception {
        var testBook = bookService.add(new Book(1L, "test", "testAuthor"));
        var expectBook = OBJECT_MAPPER.writeValueAsString(testBook);

        var result = mockMvc.perform(put(("/books/".concat(testBook.getId().toString()))).contentType(MediaType.APPLICATION_JSON)
                        .content(expectBook))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(expectBook, result);
    }

    @Test
    void itShouldDeleteBook() throws Exception {
        var testBook = bookService.add(new Book(1L, "test", "testAuthor"));

        mockMvc.perform(delete("/books/".concat(testBook.getId().toString())).contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is2xxSuccessful());

        assertNull(bookService.getBookById(testBook.getId()));
    }

    @Test
    void itShouldDeleteNothing() throws Exception {
        mockMvc.perform(delete("/books/122").contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError());
    }

    private Long getNextId() {
        return bookService.add(new Book("any", "any"))
                .getId() + 1L;
    }
}