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

	@Column(nullable = false)
	private int quantity = 1;

	private String section;

	private boolean available = true;

	private String tags; // ✅ Optional tags/genres field

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<BorrowedBook> borrowedBooks = new ArrayList<>();

	public Book() {}

	public Book(String title, String author, int quantity, String section) {
		this.title = title;
		this.author = author;
		this.section = section;
		this.quantity = quantity;
		this.available = true;
	}

	// 🔽 Getters & Setters

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

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public List<BorrowedBook> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}

	// ✅ Feature vector for KNN
	public double[] toFeatureVector() {
		double titleFeature = Objects.hash(title) % 1000;
		double authorFeature = Objects.hash(author) % 1000;
		double sectionFeature = Objects.hash(section) % 1000;
		double tagsFeature = tags != null ? Objects.hash(tags) % 1000 : 0;
		return new double[]{titleFeature, authorFeature, sectionFeature, tagsFeature};
	}

	// ✅ Aliases for analytics

	public String getCategory() {
		return section;
	}

	public void setCategory(String category) {
		this.section = category;
	}

	public String getGenre() {
		return tags;
	}

	public void setGenre(String genre) {
		this.tags = genre;
	}
}
