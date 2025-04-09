package org.tech.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate borrowDate;
	private LocalDate dueDate; // 🔥 New field

	private boolean returned;
	private String pickupMessage;

	private int fineAmount; // 🔥 New field

	@ManyToOne
	@JoinColumn(name = "book_id")
	private Book book;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public BorrowedBook() {}

	public BorrowedBook(LocalDate borrowDate, boolean returned, String pickupMessage, Book book, User user) {
		this.borrowDate = borrowDate;
		this.returned = returned;
		this.pickupMessage = pickupMessage;
		this.book = book;
		this.user = user;
		this.dueDate = borrowDate.plusDays(14); // ✅ Default logic
		this.fineAmount = 0;
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
		// Whenever borrowDate is set, also calculate dueDate
		this.dueDate = borrowDate.plusDays(14);
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isReturned() {
		return returned;
	}

	public void setReturned(boolean returned) {
		this.returned = returned;
	}

	public String getPickupMessage() {
		return pickupMessage;
	}

	public void setPickupMessage(String pickupMessage) {
		this.pickupMessage = pickupMessage;
	}

	public int getFineAmount() {
		return fineAmount;
	}

	public void setFineAmount(int fineAmount) {
		this.fineAmount = fineAmount;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
