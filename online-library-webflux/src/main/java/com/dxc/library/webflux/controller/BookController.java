package com.dxc.library.webflux.controller;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/book")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<BookDto> saveBook(@RequestBody @Valid final BookDto bookDto) {
        return bookService.saveBook(bookDto);
    }

    @GetMapping("/find/book/{isbn}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<BookDto> findBookById(@PathVariable final String isbn) {
        return bookService.findBookById(isbn);
    }

    @GetMapping("/books")
    public Flux<BookDto> getAllBooks() {
        return bookService.findAllBooks();
    }

}
