package com.library.management.controller;

import com.library.management.dto.ApiResponse;
import com.library.management.dto.AuthorRequestDto;
import com.library.management.dto.AuthorResponseDto;
import com.library.management.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorResponseDto>> create(@Valid @RequestBody AuthorRequestDto request) {
        AuthorResponseDto data = authorService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<AuthorResponseDto>builder()
                        .message("Author created successfully")
                        .data(data)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AuthorResponseDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.<List<AuthorResponseDto>>builder()
                .message("Authors fetched successfully")
                .data(authorService.findAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponseDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<AuthorResponseDto>builder()
                .message("Author fetched successfully")
                .data(authorService.findById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody AuthorRequestDto request) {
        return ResponseEntity.ok(ApiResponse.<AuthorResponseDto>builder()
                .message("Author updated successfully")
                .data(authorService.update(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        authorService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Author deleted successfully")
                .data(null)
                .build());
    }
}
