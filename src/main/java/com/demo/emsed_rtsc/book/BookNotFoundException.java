package com.demo.emsed_rtsc.book;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
    // ...
}

