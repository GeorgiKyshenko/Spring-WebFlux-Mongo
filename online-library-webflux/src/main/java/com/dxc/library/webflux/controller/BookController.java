package com.dxc.library.webflux.controller;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

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

    @PutMapping("/update/book")
    public Mono<BookDto> updateBookById(@RequestBody @Valid final BookDto bookDto) {
        return bookService.updateBookById(bookDto);
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBookById(@PathVariable final String isbn) {
        return bookService.deleteBookById(isbn);
    }

    /**
     * Short test with @RequestParam using Map
     * 
     * @param params Should be Map of String,String because it gives mapping error if String,Integer
     * @return map of key value pairs
     */
    @GetMapping("/test")
    public Map<String, String> testMethod(@RequestParam Map<String, String> params) {
        params.forEach((k,v) -> System.out.println("key: " + k + " value: " + v));
        return params;
    }
}
