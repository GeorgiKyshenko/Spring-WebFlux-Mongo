package com.dxc.library.webflux.controller;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.service.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.reactive.server.WebTestClient.*;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = BookController.class)
public class BookControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private BookService bookService;
    private BookDto bookDto;
    private BookDto updatedBookWithIsbn1;

    @BeforeEach
    void setUp() {
        bookDto = BookDto.builder()
                .isbn("1")
                .name("Test Book")
                .authorName("Test AuthorName")
                .pages(200)
                .build();

        updatedBookWithIsbn1 = BookDto.builder()
                .isbn("1")
                .name("Updated Book")
                .authorName("Updated AuthorName")
                .pages(200)
                .build();
    }


    @Test
    void givenBookObj_whenSaveBook_thenReturnSavedObj() {

        //given
        given(bookService.saveBook(any(BookDto.class))).willReturn(Mono.just(bookDto));

        //when
        final ResponseSpec response = webTestClient.post().uri("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(bookDto), BookDto.class)
                .exchange();

        //then
        response.expectStatus().isCreated()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.isbn").isEqualTo(bookDto.getIsbn())
                .jsonPath("$.name").isEqualTo(bookDto.getName())
                .jsonPath("$.authorName").isEqualTo(bookDto.getAuthorName())
                .jsonPath("$.pages").isEqualTo(bookDto.getPages());
    }

    @Test
    void givenBookId_whenGetBook_thenReturnTheBookObj() {

        //given
        given(bookService.findBookById(bookDto.getIsbn())).willReturn(Mono.just(bookDto));

        //when
        ResponseSpec response = webTestClient
                .get()
                .uri("/api/v1/find/book/{isbn}", Collections.singletonMap("isbn", bookDto.getIsbn()))
                .exchange();

        //then
        response.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.isbn").isEqualTo(bookDto.getIsbn())
                .jsonPath("$.name").isEqualTo(bookDto.getName())
                .jsonPath("$.authorName").isEqualTo(bookDto.getAuthorName())
                .jsonPath("$.pages").isEqualTo(bookDto.getPages());
    }

    @Test
    void givenBookList_whenGetAllBooks_thenReturnsListOfBook() {

        //given
        final List<BookDto> bookDtoList = List.of(bookDto, bookDto, bookDto);
        given(bookService.findAllBooks()).willReturn(Flux.fromIterable(bookDtoList));

        //when
        ResponseSpec response = webTestClient.get().uri("/api/v1/books").exchange();

        //then
        response.expectStatus()
                .isOk()
                .expectBodyList(BookDto.class)
                .consumeWith(System.out::println);
    }

    @Test
    void givenUpdatedBookObjWithId_whenUpdateBook_thenReturnTheUpdatedBook() {

        //given
        given(bookService.updateBookById(any(BookDto.class))).willReturn(Mono.just(updatedBookWithIsbn1));

        //when
        ResponseSpec response = webTestClient.put().uri("/api/v1/update/book")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(updatedBookWithIsbn1), BookDto.class)
                .exchange();

        //then
        response.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println)
                .jsonPath("$.isbn").isEqualTo(updatedBookWithIsbn1.getIsbn())
                .jsonPath("$.name").isEqualTo(updatedBookWithIsbn1.getName())
                .jsonPath("$.authorName").isEqualTo(updatedBookWithIsbn1.getAuthorName())
                .jsonPath("$.pages").isEqualTo(updatedBookWithIsbn1.getPages());
    }

    @Test
    @DisplayName("")
    void givenBookId_whenDeleteBookById_thenReturnNothing() {

        //given
        given(bookService.deleteBookById(bookDto.getIsbn())).willReturn(Mono.empty());

        //when
        ResponseSpec response = webTestClient.delete().uri("/api/v1//{isbn}", bookDto.getIsbn()).exchange();

        //then
        response.expectStatus()
                .isOk()
                .expectBody()
                .consumeWith(System.out::println);

    }
}
