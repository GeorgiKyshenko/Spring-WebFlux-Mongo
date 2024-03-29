package com.dxc.library.webflux.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BookDto {

    @NotBlank
    private String isbn;
    @NotBlank
    private String name;
    @NotBlank
    private String authorName;
    @Max(value = 1000)
    private int pages;
}
