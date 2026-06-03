package com.library.management.service.impl;

import com.library.management.config.LibraryProperties;
import com.library.management.dto.BorrowRequestDto;
import com.library.management.dto.LoanRequestDto;
import com.library.management.dto.LoanResponseDto;
import com.library.management.entity.Book;
import com.library.management.entity.Loan;
import com.library.management.exception.BookUnavailableException;
import com.library.management.exception.ResourceNotFoundException;
import com.library.management.repository.BookRepository;
import com.library.management.repository.LoanRepository;
import com.library.management.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final BookRepository bookRepository;
    private final LibraryProperties libraryProperties;

    @Override
    public LoanResponseDto create(LoanRequestDto request) {
        Book book = getBookOrThrow(request.getBookId());
        ensureBookIsAvailable(book);

        Loan loan = buildLoan(request.getBorrowerName(), request.getBorrowDate(), request.getDueDate(), book);
        markBookAsBorrowed(book);

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    @Transactional(readOnly = true)
    public List<LoanResponseDto> findAll() {
        return loanRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LoanResponseDto findById(Long id) {
        return mapToResponse(getLoanOrThrow(id));
    }

    @Override
    public LoanResponseDto update(Long id, LoanRequestDto request) {
        Loan loan = getLoanOrThrow(id);
        Book book = getBookOrThrow(request.getBookId());

        loan.setBorrowerName(request.getBorrowerName());
        loan.setBorrowDate(request.getBorrowDate());
        loan.setDueDate(request.getDueDate());
        loan.setBook(book);

        return mapToResponse(loanRepository.save(loan));
    }

    @Override
    public void delete(Long id) {
        Loan loan = getLoanOrThrow(id);
        loanRepository.delete(loan);
    }

    /**
     * Borrow a book: only allowed when available = true.
     */
    @Override
    public LoanResponseDto borrowBook(Long bookId, BorrowRequestDto request) {
        Book book = getBookOrThrow(bookId);
        ensureBookIsAvailable(book);

        LocalDate borrowDate = LocalDate.now();
        Loan loan = buildLoan(request.getBorrowerName(), borrowDate, request.getDueDate(), book);
        markBookAsBorrowed(book);

        return mapToResponse(loanRepository.save(loan));
    }

    /**
     * Return a book: set available = true, returned = true, calculate fine if late.
     */
    @Override
    public LoanResponseDto returnBook(Long loanId) {
        Loan loan = getLoanOrThrow(loanId);

        if (Boolean.TRUE.equals(loan.getReturned())) {
            throw new BookUnavailableException("This loan is already returned");
        }

        LocalDate returnDate = LocalDate.now();
        loan.setReturnDate(returnDate);
        loan.setReturned(true);
        loan.setFineAmount(calculateFine(loan.getDueDate(), returnDate));

        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepository.save(book);

        return mapToResponse(loanRepository.save(loan));
    }

    private void ensureBookIsAvailable(Book book) {
        if (!Boolean.TRUE.equals(book.getAvailable())) {
            throw new BookUnavailableException("Book is not available for borrowing: " + book.getTitle());
        }
    }

    private void markBookAsBorrowed(Book book) {
        book.setAvailable(false);
        bookRepository.save(book);
    }

    private Loan buildLoan(String borrowerName, LocalDate borrowDate, LocalDate dueDate, Book book) {
        return Loan.builder()
                .borrowerName(borrowerName)
                .borrowDate(borrowDate)
                .dueDate(dueDate)
                .returned(false)
                .fineAmount(BigDecimal.ZERO)
                .book(book)
                .build();
    }

    /**
     * Fine = number of late days × fixed amount (from application.properties).
     */
    private BigDecimal calculateFine(LocalDate dueDate, LocalDate returnDate) {
        if (!returnDate.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }

        long lateDays = ChronoUnit.DAYS.between(dueDate, returnDate);
        int finePerDay = libraryProperties.getFine().getPerDay();
        return BigDecimal.valueOf(lateDays * finePerDay);
    }

    private Book getBookOrThrow(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    private Loan getLoanOrThrow(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }

    private LoanResponseDto mapToResponse(Loan loan) {
        return LoanResponseDto.builder()
                .id(loan.getId())
                .borrowerName(loan.getBorrowerName())
                .borrowDate(loan.getBorrowDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .fineAmount(loan.getFineAmount())
                .returned(loan.getReturned())
                .bookId(loan.getBook().getId())
                .bookTitle(loan.getBook().getTitle())
                .build();
    }
}
