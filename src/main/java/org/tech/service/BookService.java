package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tech.dto.AddBookRequest;
import org.tech.dto.BookRequest;
import org.tech.entity.Book;
import org.tech.repository.BookRepository;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public String addBook(AddBookRequest request) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getSection()
        );
        book.setAvailable(true); // Always set true when adding

        bookRepository.save(book);
        return "Book added successfully!";
    }

    public Book updateBook(Long bookId, BookRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setSection(request.getSection());

        // Only update availability if explicitly passed
        book.setAvailable(request.isAvailable());

        return bookRepository.save(book);
    }

    public String deleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        bookRepository.delete(book);
        return "Book deleted successfully!";
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}