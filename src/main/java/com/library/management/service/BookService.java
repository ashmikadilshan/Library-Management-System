package com.library.management.service;

import com.library.management.dto.BookRequestDto;
import com.library.management.dto.BookResponseDto;

import java.util.List;

public interface BookService {

    BookResponseDto create(BookRequestDto request);

    List<BookResponseDto> findAll();

    BookResponseDto findById(Long id);

    BookResponseDto update(Long id, BookRequestDto request);

    void delete(Long id);
}
