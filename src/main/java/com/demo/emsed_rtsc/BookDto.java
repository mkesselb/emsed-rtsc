package com.demo.emsed_rtsc;

public class BookDto {

    private Long id;

    private String title;

    private String author;

    public long getId() {
        return this.id;
    }
    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public void setId(long id) {
        this.id = id;
    }
}

