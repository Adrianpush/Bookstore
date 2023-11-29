package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Table(name = "order_items")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "quantity")
    private int quantity;
}
