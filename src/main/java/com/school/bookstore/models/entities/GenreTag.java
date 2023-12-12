package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "genre")
@Data
public class GenreTag implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "genre")
    private String genre;
}