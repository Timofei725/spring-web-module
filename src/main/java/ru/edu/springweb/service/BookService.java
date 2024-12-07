package ru.edu.springweb.service;

import ru.edu.springweb.model.Book;

import java.util.List;

public interface BookService {
    List<Book> getAll();

    Book getBookById(Long bookId);

    Book add(Book book);

    Book update(Long bookId, Book book);

    boolean delete(Long id);
}