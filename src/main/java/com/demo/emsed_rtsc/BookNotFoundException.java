package com.demo.emsed_rtsc;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }
    // ...
}

