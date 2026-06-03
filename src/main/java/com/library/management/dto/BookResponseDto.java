package com.library.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {

    private Long id;
    private String title;
    private String isbn;
    private Boolean available;
    private Long authorId;
    private String authorName;
}
