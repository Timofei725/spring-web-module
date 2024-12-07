package ru.edu.springweb.service;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import ru.edu.springweb.model.Book;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class SimpleBookService implements BookService {
    private static final Map<Long, Book> STORE = new ConcurrentHashMap<>();
    private static final AtomicLong SEQUENCE = new AtomicLong(0L);

    @Override
    public List<Book> getAll() {
        return STORE.values()
                .stream()
                .toList();
    }

    @Nullable
    @Override
    public Book getBookById(Long bookId) {
        return STORE.get(bookId);
    }

    @Override
    public Book add(Book book) {
        book.setId(SEQUENCE.incrementAndGet());
        STORE.put(book.getId(), book);
        return book;
    }

    @Nullable
    @Override
    public Book update(Long bookId, Book book) {
        if (STORE.containsKey(bookId)) {
            book.setId(bookId);
            STORE.put(bookId, book);
            return book;
        }
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return Optional.ofNullable(STORE.remove(id))
                .isPresent();
    }
}