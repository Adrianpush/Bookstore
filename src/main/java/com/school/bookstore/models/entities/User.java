package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    Long id;
    @Column
    String firstName;
    @Column
    String lastName;
    @Column
    String eMail;
    @ManyToMany
    @JoinTable(
            name = "favorite_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    List<Book> favoriteBooks;

}
