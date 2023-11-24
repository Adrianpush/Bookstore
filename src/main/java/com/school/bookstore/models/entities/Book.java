package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue
    Long id;

    @Column(name = "title")
    String title;

    @ManyToMany
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    List<Author> authors;

    @Column(name = "publisher")
    String publisher;

    @ManyToMany
    @JoinTable(
            name = "books_tags",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    Set<GenreTag> genreTagSet;

    @Column(name = "year_published")
    int yearPublished;

    @Column(name = "description")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    Language language;

    @Column(name = "num_pages")
    int numPages;

    @Column(name = "price_before_discount")
    double priceBeforeDiscount;

    @Column(name = "discount_percentage")
    int discountPercent;

    @Column(name = "copies_available")
    int copiesAvailable;
}
