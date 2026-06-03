package com.library.management.service;

import com.library.management.dto.AuthorRequestDto;
import com.library.management.dto.AuthorResponseDto;

import java.util.List;

public interface AuthorService {

    AuthorResponseDto create(AuthorRequestDto request);

    List<AuthorResponseDto> findAll();

    AuthorResponseDto findById(Long id);

    AuthorResponseDto update(Long id, AuthorRequestDto request);

    void delete(Long id);
}
