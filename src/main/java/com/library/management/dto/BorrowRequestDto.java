package com.library.management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Used when borrowing a book via POST /api/loans/borrow/{bookId}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowRequestDto {

    @NotBlank(message = "Borrower name is required")
    private String borrowerName;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;
}
