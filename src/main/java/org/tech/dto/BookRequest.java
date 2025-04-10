package org.tech.dto;

public class BookRequest {
    private String title;
    private String author;
    private String section;
    private boolean available;
    private int quantity;      // ✅ New field
    private String tags;       // ✅ New field

    public BookRequest() {}

    public BookRequest(String title, String author, String section, boolean available) {
        this.title = title;
        this.author = author;
        this.section = section;
        this.available = available;
    }

    // Getters and Setters

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
