package com.library.management.service.impl;

import com.library.management.dto.BookRequestDto;
import com.library.management.dto.BookResponseDto;
import com.library.management.entity.Author;
import com.library.management.entity.Book;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.AuthorRepository;
import com.library.management.repository.BookRepository;
import com.library.management.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Override
    public BookResponseDto create(BookRequestDto request) {
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.getAuthorId()));

        Book book = Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .available(true)
                .author(author)
                .build();

        return mapToResponse(bookRepository.save(book));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookResponseDto> findAll() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponseDto findById(Long id) {
        return mapToResponse(getBookOrThrow(id));
    }

    @Override
    public BookResponseDto update(Long id, BookRequestDto request) {
        Book book = getBookOrThrow(id);
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.getAuthorId()));

        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setAuthor(author);
        return mapToResponse(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        Book book = getBookOrThrow(id);
        bookRepository.delete(book);
    }

    private Book getBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private BookResponseDto mapToResponse(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .available(book.getAvailable())
                .authorId(book.getAuthor().getId())
                .authorName(book.getAuthor().getName())
                .build();
    }
}
