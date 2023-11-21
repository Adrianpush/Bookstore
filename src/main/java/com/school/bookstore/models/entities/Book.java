package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue
    Long id;
    @Column
    String title;
    @Column
    String author;
    @Column
    String description;
    @Column
    String genre;
    @Column
    int numPages;
    @ManyToMany(mappedBy = "favoriteBooks")
    List<User> favorites;
}
