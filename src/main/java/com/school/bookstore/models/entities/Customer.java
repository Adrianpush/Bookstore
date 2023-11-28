package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "customers")
@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Transient
    private List<OrderItem> shoppingCart;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Order> orderHistory;
}
