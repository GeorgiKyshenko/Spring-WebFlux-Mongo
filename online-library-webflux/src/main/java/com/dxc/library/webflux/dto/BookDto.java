package com.dxc.library.webflux.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDto {
    private String isbn;
    private String name;
    private String authorName;
    private int pages;
}
