package com.library.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Loan entity.
 * Each loan is linked to one Book (Many-to-One).
 */
@Entity
@Table(name = "loans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String borrowerName;

    @Column(nullable = false)
    private LocalDate borrowDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    /**
     * Set when the book is returned; null while loan is active.
     */
    private LocalDate returnDate;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal fineAmount = BigDecimal.ZERO;

    /**
     * false = book still borrowed; true = returned.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean returned = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}
