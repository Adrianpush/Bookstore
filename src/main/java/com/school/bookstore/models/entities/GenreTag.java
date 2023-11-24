package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "genre")
@Data
public class GenreTag {

    @Id
    @GeneratedValue
    Long id;
    @Column(name = "genre")
    String genre;
}
