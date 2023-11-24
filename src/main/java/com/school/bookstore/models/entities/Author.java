package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "authors")
@Data
public class Author {

    @Id
    @GeneratedValue
    Long id;
    @Column(name = "full_name")
    String fullName;
    @Column(name = "author_information")
    String authorInformation;
}
