package org.tech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tech.dto.BorrowedBookDTO;
import org.tech.entity.BorrowedBook;
import org.tech.service.BorrowService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/books")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/borrow/{bookId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<String> borrowBook(@PathVariable Long bookId, Authentication authentication) {
        String email = authentication.getName();
        String message = borrowService.borrowBook(bookId, email);
        return ResponseEntity.ok(message);
    }

    @PostMapping("/return/{borrowId}")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<String> returnBook(@PathVariable Long borrowId) {
        String message = borrowService.returnBook(borrowId);
        return ResponseEntity.ok(message);
    }

    // ✅ STUDENT: Get all borrowed books
    @GetMapping("/borrowed")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<List<BorrowedBook>> getMyBorrowedBooks(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(borrowService.getBorrowedBooksByUser(email));
    }

    // ✅ STUDENT: Filter by returned status (true/false)
    @GetMapping("/borrowed/filter")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public ResponseEntity<List<BorrowedBook>> getMyFilteredBorrows(
            @RequestParam boolean returned,
            Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(borrowService.getBorrowedBooksByUserAndReturned(email, returned));
    }

    @GetMapping("/admin/borrowed")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BorrowedBookDTO>> getAllBorrowedBooks() {
        List<BorrowedBookDTO> dtoList = borrowService.getAllBorrowedBooks();
        return ResponseEntity.ok(dtoList);
    }



    // ✅ ADMIN: Filter by returned status (true/false)
    @GetMapping("/admin/borrowed/filter")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BorrowedBook>> getAdminFilteredBorrows(@RequestParam boolean returned) {
        return ResponseEntity.ok(borrowService.getBorrowedBooksByReturnedStatus(returned));
    }

    // ✅ ADMIN: Get overdue books (not returned and due date passed)
    @GetMapping("/admin/borrowed/overdue")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<BorrowedBook>> getOverdueBooks() {
        return ResponseEntity.ok(borrowService.getOverdueBooks());
    }
}
