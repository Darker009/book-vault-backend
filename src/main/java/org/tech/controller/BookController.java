package org.tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.tech.dto.AddBookRequest;
import org.tech.dto.BookRequest;
import org.tech.entity.Book;
import org.tech.service.BookService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Accessible only to ADMIN
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody AddBookRequest request) {
        String message = bookService.addBook(request);
        return ResponseEntity.ok(message);
    }

    // Accessible only to ADMIN
    @PutMapping("/{bookId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody BookRequest request) {
        Book updated = bookService.updateBook(bookId, request);
        return ResponseEntity.ok(updated);
    }

    // Accessible only to ADMIN
    @DeleteMapping("/{bookId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        System.out.println("Attempting to delete book with ID: " + bookId); // Debug log
        String message = bookService.deleteBook(bookId);
        return ResponseEntity.ok(message);
    }
    // Accessible to both ADMIN and STUDENT
    @GetMapping("/{bookId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(book);
    }

    // Accessible to both ADMIN and STUDENT
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getBookStatistics() {
        return ResponseEntity.ok(bookService.getBookStatistics());
    }

}
