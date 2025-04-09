package org.tech.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.tech.entity.Book;
import org.tech.entity.BorrowedBook;
import org.tech.entity.User;
import org.tech.repository.BookRepository;
import org.tech.repository.BorrowedBookRepository;
import org.tech.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

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

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is currently unavailable.");
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

        book.setAvailable(false);
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
        book.setAvailable(true);
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

    // ✅ Admin: All borrows
    public List<BorrowedBook> getAllBorrowedBooks() {
        return borrowedBookRepository.findAll();
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

    // You can use this method in frontend as well when you expose dueDate
}
