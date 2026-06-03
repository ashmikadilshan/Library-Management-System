package com.library.management.controller;

import com.library.management.dto.ApiResponse;
import com.library.management.dto.BookRequestDto;
import com.library.management.dto.BookResponseDto;
import com.library.management.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDto>> create(@Valid @RequestBody BookRequestDto request) {
        BookResponseDto data = bookService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<BookResponseDto>builder()
                        .message("Book created successfully")
                        .data(data)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponseDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.<List<BookResponseDto>>builder()
                .message("Books fetched successfully")
                .data(bookService.findAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<BookResponseDto>builder()
                .message("Book fetched successfully")
                .data(bookService.findById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDto request) {
        return ResponseEntity.ok(ApiResponse.<BookResponseDto>builder()
                .message("Book updated successfully")
                .data(bookService.update(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Book deleted successfully")
                .data(null)
                .build());
    }
}
