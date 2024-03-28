package com.dxc.library.webflux.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Document(collection = "books")
public class Book {

    //if isbn(id) is not passed, the DB generates some UUID
    @Id
    private String isbn;
    private String name;
    private String authorName;
    private int pages;
}
