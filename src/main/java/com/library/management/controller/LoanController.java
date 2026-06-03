package com.library.management.controller;

import com.library.management.dto.ApiResponse;
import com.library.management.dto.BorrowRequestDto;
import com.library.management.dto.LoanRequestDto;
import com.library.management.dto.LoanResponseDto;
import com.library.management.service.LoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<ApiResponse<LoanResponseDto>> create(@Valid @RequestBody LoanRequestDto request) {
        LoanResponseDto data = loanService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<LoanResponseDto>builder()
                        .message("Loan created successfully")
                        .data(data)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LoanResponseDto>>> findAll() {
        return ResponseEntity.ok(ApiResponse.<List<LoanResponseDto>>builder()
                .message("Loans fetched successfully")
                .data(loanService.findAll())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanResponseDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<LoanResponseDto>builder()
                .message("Loan fetched successfully")
                .data(loanService.findById(id))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LoanResponseDto>> update(
            @PathVariable Long id,
            @Valid @RequestBody LoanRequestDto request) {
        return ResponseEntity.ok(ApiResponse.<LoanResponseDto>builder()
                .message("Loan updated successfully")
                .data(loanService.update(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        loanService.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Loan deleted successfully")
                .data(null)
                .build());
    }

    @PostMapping("/borrow/{bookId}")
    public ResponseEntity<ApiResponse<LoanResponseDto>> borrowBook(
            @PathVariable Long bookId,
            @Valid @RequestBody BorrowRequestDto request) {
        LoanResponseDto data = loanService.borrowBook(bookId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<LoanResponseDto>builder()
                        .message("Book borrowed successfully")
                        .data(data)
                        .build());
    }

    @PostMapping("/return/{loanId}")
    public ResponseEntity<ApiResponse<LoanResponseDto>> returnBook(@PathVariable Long loanId) {
        LoanResponseDto data = loanService.returnBook(loanId);
        return ResponseEntity.ok(ApiResponse.<LoanResponseDto>builder()
                .message("Book returned successfully")
                .data(data)
                .build());
    }
}
