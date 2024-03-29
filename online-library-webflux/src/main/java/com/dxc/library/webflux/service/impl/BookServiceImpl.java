package com.dxc.library.webflux.service.impl;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.entity.Book;
import com.dxc.library.webflux.mapper.BookMapper;
import com.dxc.library.webflux.repository.BookRepository;
import com.dxc.library.webflux.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<BookDto> findBookById(final String isbn) {

        final Mono<Book> book = bookRepository.findById(isbn);
        return book.map(BookMapper::mapToBookDto);
    }

    @Override
    public Flux<BookDto> findAllBooks() {

        final Flux<Book> books = bookRepository.findAll();
        return books.map(BookMapper::mapToBookDto);
    }
}
