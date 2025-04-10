package org.tech.dto;

import org.tech.entity.BorrowedBook;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowedBookDTO {
    private Long id;
    private String bookTitle;
    private String userEmail;
    private String studentName; // ✅ Add this
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean returned;
    private boolean overdue;
    private long daysRemaining;

    public BorrowedBookDTO(BorrowedBook borrowedBook) {
        this.id = borrowedBook.getId();
        this.bookTitle = borrowedBook.getBook().getTitle();
        this.userEmail = borrowedBook.getUser().getEmail();
        if (borrowedBook.getUser().getUserProfile() != null) {
            this.studentName = borrowedBook.getUser().getUserProfile().getFullName();
        } else {
            this.studentName = "N/A"; // or you can set it to null
        }
        this.borrowDate = borrowedBook.getBorrowDate();
        this.returned = borrowedBook.isReturned();

        this.dueDate = borrowedBook.getBorrowDate().plusDays(14);
        this.overdue = !returned && dueDate.isBefore(LocalDate.now());
        this.daysRemaining = !returned ? ChronoUnit.DAYS.between(LocalDate.now(), dueDate) : 0;
    }

    public String getStudentName() {
        return studentName;
    }
    public BorrowedBookDTO() {
        // No-args constructor
    }


    // Getters
    public Long getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isReturned() {
        return returned;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }
}
