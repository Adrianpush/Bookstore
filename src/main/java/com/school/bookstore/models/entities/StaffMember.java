package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "staff")
@Entity
public class StaffMember {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
}
