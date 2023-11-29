package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "genre")
@Data
public class GenreTag {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "genre")
    private String genre;
}
