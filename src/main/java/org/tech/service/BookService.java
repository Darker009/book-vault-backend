package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tech.dto.AddBookRequest;
import org.tech.dto.BookRequest;
import org.tech.entity.Book;
import org.tech.repository.BookRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public String addBook(AddBookRequest request) {
        Book book = new Book(
                request.getTitle(),
                request.getAuthor(),
                request.getQuantity(),
                request.getSection()
        );

        book.setAvailable(request.getQuantity() > 0); // ✅ Set availability based on quantity
        book.setTags(request.getTags());              // ✅ Set tags

        bookRepository.save(book);
        return "Book added successfully!";
    }

    public Book updateBook(Long bookId, BookRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setSection(request.getSection());
        book.setQuantity(request.getQuantity());          // ✅ Update quantity
        book.setTags(request.getTags());                  // ✅ Update tags
        book.setAvailable(request.getQuantity() > 0);     // ✅ Re-evaluate availability

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
    public Map<String, Object> getBookStatistics() {
        List<Book> allBooks = bookRepository.findAll();

        Map<String, Long> byCategory = allBooks.stream()
                .collect(Collectors.groupingBy(Book::getCategory, Collectors.counting()));

        Map<String, Long> byGenre = allBooks.stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));

        long totalBooks = allBooks.size();

        Map<String, Object> result = new HashMap<>();
        result.put("totalBooks", totalBooks);
        result.put("categoryCount", byCategory);
        result.put("genreCount", byGenre);

        return result;
    }

    public int getTotalBooks() {
        return bookRepository.findAll().size();
    }

    public Map<String, Long> getBooksByGenre() {
        return bookRepository.findAll().stream()
                .collect(Collectors.groupingBy(Book::getGenre, Collectors.counting()));
    }


}
