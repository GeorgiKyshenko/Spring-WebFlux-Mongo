package com.dxc.library.webflux.mapper;

import com.dxc.library.webflux.dto.BookDto;
import com.dxc.library.webflux.entity.Book;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public class BookMapper {

    private BookMapper() {
    }

    public static BookDto mapToBookDto(final Book book) {
        return BookDto.builder()
                .isbn(book.getIsbn())
                .name(book.getName())
                .authorName(book.getAuthorName())
                .pages(book.getPages())
                .build();
    }

    public static Book mapToBook(final BookDto bookDto) {
        return Book.builder()
                .isbn(bookDto.getIsbn())
                .name(bookDto.getName())
                .authorName(bookDto.getAuthorName())
                .pages(bookDto.getPages())
                .build();
    }

    /* Experimenting my things, to recall my knowledge in Reflection (Not a good practice, no exception handling etc.)*/
    public static <From, To> To FromBookToBookDtoOrBackwards(final From from, Class<To> toClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchFieldException {

        Arrays.stream(toClass.getDeclaredConstructors()).forEach(constructor -> constructor.setAccessible(true));
        Constructor<?> constructor = toClass.getDeclaredConstructor(String.class, String.class, String.class, int.class);


        To to = (To) constructor.newInstance("isbn", "name", "authorName", 0);

        for (Field fromField : from.getClass().getDeclaredFields()) {
            fromField.setAccessible(true);
            for (Field toField : to.getClass().getDeclaredFields()) {
                toField.setAccessible(true);
                if (fromField.getName().equals(toField.getName())) {
                    toField.set(to, fromField.get(from));
                }
            }
        }
        return to;
    }
}
