package com.dxc.library.webflux.integration;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.repository.BookRepository;
import com.dxc.library.webflux.service.BookService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    }

    @AfterEach
    void tearDown() {
        /*With @BeforeEach the last method which have saveBook inside leave the record in the DB to prevent that, maybe we need
         * to use @AfterEach and call bookRepository.deleteALl().subscribe() and remove the @Order annotation functionality*/
        System.out.println("===> Start: Deleting records before each");
        bookRepository.deleteAll().subscribe();
        System.out.println("===> End: Deleting complete");
    }

    @Test
    @Order(1)
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
    @Order(3)
    void testGetSingleBook() {
        
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
    @Order(2)
    void testGetAllBooks() {
        final BookDto bookToSave = BookDto.builder()
                .isbn("100")
                .name("Book")
                .authorName("AuthorName")
                .pages(200)
                .build();

        // we need to save some books in order to get a list of books from getAllBooks method, because we delete them in BeforeEach annotated method!
        bookService.saveBook(bookDto).block();
        bookService.saveBook(bookToSave).block();

        webTestClient.get().uri("/api/v1/books")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BookDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    void updateBookById() {

        final BookDto savedBook = bookService.saveBook(bookDto).block();
        final BookDto updatedBookResponse = BookDto.builder()
                .isbn(savedBook.getIsbn())
                .name("Update Name")
                .authorName("Update Author")
                .pages(358)
                .build();

        System.out.println("===> " + bookRepository.findById(savedBook.getIsbn()).block());

        webTestClient.put().uri("/api/v1/update/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedBookResponse), BookDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.isbn").isEqualTo(updatedBookResponse.getIsbn())
                .jsonPath("$.name").isEqualTo(updatedBookResponse.getName())
                .jsonPath("$.authorName").isEqualTo(updatedBookResponse.getAuthorName())
                .jsonPath("$.pages").isEqualTo(updatedBookResponse.getPages());
    }

    @Test
    void testDeleteBookById() {

        final BookDto savedBook = bookService.saveBook(bookDto).block();

        webTestClient.delete().uri("/api/v1/{isbn}", savedBook.getIsbn())
                .exchange()
                .expectStatus().isNoContent()
                .expectBody()
                .consumeWith(System.out::println);
    }
}
