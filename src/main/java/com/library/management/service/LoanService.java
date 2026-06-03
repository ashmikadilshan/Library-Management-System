package com.library.management.service;

import com.library.management.dto.BorrowRequestDto;
import com.library.management.dto.LoanRequestDto;
import com.library.management.dto.LoanResponseDto;

import java.util.List;

public interface LoanService {

    LoanResponseDto create(LoanRequestDto request);

    List<LoanResponseDto> findAll();

    LoanResponseDto findById(Long id);

    LoanResponseDto update(Long id, LoanRequestDto request);

    void delete(Long id);

    LoanResponseDto borrowBook(Long bookId, BorrowRequestDto request);

    LoanResponseDto returnBook(Long loanId);
}
