package org.tech.dto;

public class BookRequest {
    private String title;
    private String author;
    private String section;
    private boolean available;

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
}
