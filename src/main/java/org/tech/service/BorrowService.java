package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tech.dto.BorrowedBookDTO;
import org.tech.entity.Book;
import org.tech.entity.BorrowedBook;
import org.tech.entity.User;
import org.tech.repository.BookRepository;
import org.tech.repository.BorrowedBookRepository;
import org.tech.repository.UserRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowedBookRepository borrowedBookRepository;

    public String borrowBook(Long bookId, String userEmail) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with ID: " + bookId));

        if (book.getQuantity() <= 0) {
            throw new RuntimeException("Book is currently out of stock.");
        }

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        long activeBorrows = borrowedBookRepository.countByUserAndReturnedFalse(user);
        if (activeBorrows >= 3) {
            throw new RuntimeException("You can't borrow more than 3 books.");
        }

        BorrowedBook borrowedBook = new BorrowedBook();
        borrowedBook.setBook(book);
        borrowedBook.setUser(user);
        borrowedBook.setBorrowDate(LocalDate.now());
        borrowedBook.setReturned(false);

        BorrowedBook savedBorrow = borrowedBookRepository.save(borrowedBook);

        // ✅ Update quantity
        book.setQuantity(book.getQuantity() - 1);

        // ✅ If quantity is 0, mark unavailable
        if (book.getQuantity() == 0) {
            book.setAvailable(false);
        }

        bookRepository.save(book);

        return "Book borrowed successfully! Borrow ID: " + savedBorrow.getId();
    }

    public String returnBook(Long borrowId) {
        BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowId)
                .orElseThrow(() -> new RuntimeException("Borrow record not found with ID: " + borrowId));

        if (borrowedBook.isReturned()) {
            return "Book already returned.";
        }

        borrowedBook.setReturned(true);
        borrowedBookRepository.save(borrowedBook);

        Book book = borrowedBook.getBook();

        // ✅ Increase quantity
        book.setQuantity(book.getQuantity() + 1);

        // ✅ Always mark available if quantity ≥ 1
        if (book.getQuantity() >= 1) {
            book.setAvailable(true);
        }

        bookRepository.save(book);

        return "Book returned successfully!";
    }

    // ✅ Student: All borrows
    public List<BorrowedBook> getBorrowedBooksByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return borrowedBookRepository.findByUser(user);
    }

    // ✅ Student: Filter by return status
    public List<BorrowedBook> getBorrowedBooksByUserAndReturned(String email, boolean returned) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));
        return borrowedBookRepository.findByUserAndReturned(user, returned);
    }

    public List<BorrowedBookDTO> getAllBorrowedBooks() {
        List<BorrowedBook> borrowedBooks = borrowedBookRepository.findAll();

        return borrowedBooks.stream()
                .map(BorrowedBookDTO::new)
                .collect(Collectors.toList());
    }


    private boolean calculateOverdue(LocalDate dueDate, boolean returned) {
        return !returned && dueDate.isBefore(LocalDate.now());
    }


    // ✅ Admin: Filter returned or not
    public List<BorrowedBook> getBorrowedBooksByReturnedStatus(boolean returned) {
        return borrowedBookRepository.findByReturned(returned);
    }

    // ✅ Overdue detection (books borrowed more than 14 days ago & not returned)
    public List<BorrowedBook> getOverdueBooks() {
        LocalDate cutoffDate = LocalDate.now().minusDays(14);
        return borrowedBookRepository.findByReturnedFalseAndDueDateBefore(cutoffDate);
    }

    // ✅ Utility: Calculate due date for display (14 days after borrowDate)
    public LocalDate calculateDueDate(BorrowedBook borrowedBook) {
        return borrowedBook.getBorrowDate().plusDays(14);
    }

    // ✅ Admin Dashboard: Borrow stats per student (email -> count)
    public Map<String, Long> getBorrowStatsByStudent() {
        List<BorrowedBook> allBorrows = borrowedBookRepository.findAll();

        return allBorrows.stream()
                .collect(Collectors.groupingBy(
                        borrow -> borrow.getUser().getEmail(),
                        Collectors.counting()
                ));
    }

    // ✅ Admin Dashboard: Borrow summary
    public Map<String, Object> getBorrowSummary() {
        List<BorrowedBook> borrows = borrowedBookRepository.findAll();

        long totalBorrows = borrows.size();
        long activeBorrows = borrows.stream()
                .filter(b -> !b.isReturned())
                .count();
        long overdueCount = borrows.stream()
                .filter(b -> !b.isReturned() &&
                        b.getBorrowDate().plusDays(14).isBefore(LocalDate.now()))
                .count();

        Map<String, Object> summary = new HashMap<>();
        summary.put("totalBorrows", totalBorrows);
        summary.put("activeBorrows", activeBorrows);
        summary.put("overdueCount", overdueCount);

        return summary;
    }
}
