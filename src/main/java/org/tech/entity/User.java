package org.tech.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	// Stored as "ROLE_ADMIN" or "ROLE_STUDENT" only
	@Column(nullable = false)
	private String role;

	private boolean profileUpdated = false;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonManagedReference
	private UserProfile userProfile;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<BorrowedBook> borrowedBooks = new ArrayList<>();

	public User() {}

	public User(String email, String password, String role) {
		this.email = email;
		this.password = password;
		setRole(role); // normalize and validate role
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	/**
	 * Ensures role is stored as "ROLE_ADMIN" or "ROLE_STUDENT" only.
	 */
	public void setRole(String role) {
		String normalized = role.toUpperCase().replace("ROLE_", "");
		switch (normalized) {
			case "ADMIN", "STUDENT" -> this.role = "ROLE_" + normalized;
			default -> throw new IllegalArgumentException("Invalid role: must be ADMIN or STUDENT");
		}
	}

	public boolean isProfileUpdated() {
		return profileUpdated;
	}

	public void setProfileUpdated(boolean profileUpdated) {
		this.profileUpdated = profileUpdated;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public List<BorrowedBook> getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(List<BorrowedBook> borrowedBooks) {
		this.borrowedBooks = borrowedBooks;
	}
}
