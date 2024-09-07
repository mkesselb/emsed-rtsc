package com.demo.emsed_rtsc.book;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface BookService extends CrudRepository<Book, Long> {
    List<Book> findByTitle(String title);
}
