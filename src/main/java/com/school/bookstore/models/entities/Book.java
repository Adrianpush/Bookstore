package com.school.bookstore.models.entities;

import com.school.bookstore.models.enums.Language;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "books")
public class Book implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @Column(name = "publisher")
    private String publisher;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_tags",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<GenreTag> genreTagSet;

    @Column(name = "year_published")
    private int yearPublished;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "language")
    private Language language;

    @Column(name = "num_pages")
    private int numPages;

    @Column(name = "price_before_discount")
    private double priceBeforeDiscount;

    @Column(name = "discount_percentage")
    private int discountPercent;

    @Column(name = "copies_available")
    private int copiesAvailable;

    @Column(name = "image_link")
    private String imageLink;
}