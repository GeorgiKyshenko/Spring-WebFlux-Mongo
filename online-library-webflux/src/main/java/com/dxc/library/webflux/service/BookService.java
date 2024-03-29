package com.dxc.library.webflux.service;

import com.dxc.library.webflux.dto.BookDto;
import reactor.core.publisher.Mono;

public interface BookService {

    Mono<BookDto> saveBook(final BookDto bookDto);

    Mono<BookDto> findBookById(String isbn);
}
