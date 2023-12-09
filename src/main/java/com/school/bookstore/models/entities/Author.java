package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "authors")
@Data
public class Author implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "author_information", length = 5000)
    private String authorInformation;
}
