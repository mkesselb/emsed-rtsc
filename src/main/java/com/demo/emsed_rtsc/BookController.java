package com.demo.emsed_rtsc;

import java.util.List;
import static java.util.stream.Collectors.toList;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ModelMapper mapper;

    public BookController(BookService bookService, ModelMapper mapper) {
        this.bookService = bookService;
        this.mapper = mapper;
    }

    @GetMapping
    public Iterable<Book> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/title/{bookTitle}")
    public List<BookDto> findByTitle(@PathVariable String bookTitle) {
        return bookService.findByTitle(bookTitle).stream().map(this::convertToDto).collect(toList());
    }

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookService.findById(id).orElseThrow(() -> new BookNotFoundException("book not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody BookDto bookDto) {
        Book book = this.convertToEntity(bookDto);
        return bookService.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookService.findById(id).orElseThrow(() -> new BookNotFoundException("book not found"));
        bookService.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
          throw new BookIdMismatchException("book has wrong id");
        }
        bookService.findById(id).get();
        return bookService.save(book);
    }

    // https://www.baeldung.com/entity-to-and-from-dto-for-a-java-spring-application
    // https://www.geeksforgeeks.org/how-to-use-modelmapper-in-spring-boot-with-example-project/
    public BookDto convertToDto(Book book) {
        BookDto bookDto = this.mapper.map(book, BookDto.class);
        return bookDto;
    }

    public Book convertToEntity(BookDto dto) {
        Book book = this.mapper.map(dto, Book.class);
        return book;
    }

}

