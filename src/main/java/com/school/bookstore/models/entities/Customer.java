package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Table(name = "customers")
@Entity
public class Customer {

    @Id
    @GeneratedValue
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
    @OneToMany(
            mappedBy = "customer",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    private List<Order> orderHistory;
}
