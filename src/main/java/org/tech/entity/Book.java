package org.tech.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String author;

	private String section;

	private boolean available = true;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<BorrowedBook> borrowedBooks = new ArrayList<>();

	public Book() {}

	public Book(String title, String author, String section) {
		this.title = title;
		this.author = author;
		this.section = section;
		this.available = true;
	}

	// Getters and Setters

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public List<BorrowedBook> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	// ✅ Feature vector generation for KNN
	public double[] toFeatureVector() {
		// Simple hash-based conversion to numeric values
		double titleFeature = Objects.hash(title) % 1000;
		double authorFeature = Objects.hash(author) % 1000;
		double sectionFeature = Objects.hash(section) % 1000;
		return new double[]{titleFeature, authorFeature, sectionFeature};
	}
}
