package com.dxc.library.webflux.service.impl;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.entity.Book;
import com.dxc.library.webflux.mapper.BookMapper;
import com.dxc.library.webflux.repository.BookRepository;
import com.dxc.library.webflux.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    @Override
    public Mono<BookDto> saveBook(BookDto bookDto) {

        final Book book = BookMapper.mapToBook(bookDto);
        final Mono<Book> savedBook = bookRepository.save(book);
        return savedBook.map(BookMapper::mapToBookDto);
    }
}
