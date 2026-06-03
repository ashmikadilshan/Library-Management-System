package com.library.management.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Book entity.
 * Many Books belong to one Author (Many-to-One).
 */
@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, unique = true)
    private String isbn;

    /**
     * true = book is on shelf and can be borrowed.
     * false = book is currently on loan.
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean available = true;

    /**
     * Many books -> one author.
     * author_id column is created in books table.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Loan> loans = new ArrayList<>();
}
