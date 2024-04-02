package com.dxc.library.webflux.controller;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.service.BookService;
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
import reactor.core.publisher.Mono;

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


    @Test
    @DisplayName("")
    void givenBookObj_whenSaveBook_thenReturnSavedObj() {

        //given
        final BookDto bookDto = BookDto.builder()
                .isbn("1")
                .name("Saved Book")
                .authorName("Saved AuthorName")
                .pages(200)
                .build();

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
}
