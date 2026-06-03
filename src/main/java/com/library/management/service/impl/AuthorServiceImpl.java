package com.library.management.service.impl;

import com.library.management.dto.AuthorRequestDto;
import com.library.management.dto.AuthorResponseDto;
import com.library.management.entity.Author;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.AuthorRepository;
import com.library.management.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponseDto create(AuthorRequestDto request) {
        Author author = Author.builder()
                .name(request.getName())
                .email(request.getEmail())
                .build();
        return mapToResponse(authorRepository.save(author));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuthorResponseDto> findAll() {
        return authorRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthorResponseDto findById(Long id) {
        return mapToResponse(getAuthorOrThrow(id));
    }

    @Override
    public AuthorResponseDto update(Long id, AuthorRequestDto request) {
        Author author = getAuthorOrThrow(id);
        author.setName(request.getName());
        author.setEmail(request.getEmail());
        return mapToResponse(authorRepository.save(author));
    }

    @Override
    public void delete(Long id) {
        Author author = getAuthorOrThrow(id);
        authorRepository.delete(author);
    }

    private Author getAuthorOrThrow(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    private AuthorResponseDto mapToResponse(Author author) {
        return AuthorResponseDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }
}
