package com.dxc.library.webflux.integration;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.repository.BookRepository;
import com.dxc.library.webflux.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerIT {

    @Autowired
    private BookService bookService;

    @Autowired
    private WebTestClient webTestClient;

    private BookDto bookDto;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookDto = BookDto.builder().isbn("1").name("Test Book").authorName("Test AuthorName").pages(200).build();

        System.out.println("===> Start: Deleting records before each");
        bookRepository.deleteAll().subscribe();
        System.out.println("===> End: Deleting complete");

    }

    @Test
    void testSaveBook() {

        webTestClient.post().uri("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookDto), BookDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.isbn").isEqualTo(bookDto.getIsbn())
                .jsonPath("$.name").isEqualTo(bookDto.getName())
                .jsonPath("$.authorName").isEqualTo(bookDto.getAuthorName())
                .jsonPath("$.pages").isEqualTo(bookDto.getPages());
    }

    @Test
    void testGetSingleBook() {

        /* For this case I decided to use already existing book to the DB instead of creating a new book here and save it,
         * but it`s better to do it here */

        //For example:
        final BookDto bookToSave = BookDto.builder()
                .isbn("100")
                .name("Get Book")
                .authorName("Get AuthorName")
                .pages(200)
                .build();

        //bookService.saveBook(bookToSave) returns Mono so when we call .block() basically it can be transformed to BookDto or whatever the object is!
        final BookDto savedBookResponse = bookService.saveBook(bookToSave).block();
        //the ID of savedBookResponse is passed to the uri as path variable.

        webTestClient.get().uri("/api/v1/find/book/{isbn}", Objects.requireNonNull(savedBookResponse.getIsbn()))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.isbn").isEqualTo(savedBookResponse.getIsbn())
                .jsonPath("$.name").isEqualTo(savedBookResponse.getName())
                .jsonPath("$.authorName").isEqualTo(savedBookResponse.getAuthorName())
                .jsonPath("$.pages").isEqualTo(savedBookResponse.getPages());
    }

    @Test
    void testGetAllBooks() {

        webTestClient.get().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .consumeWith(System.out::println);
    }
}
