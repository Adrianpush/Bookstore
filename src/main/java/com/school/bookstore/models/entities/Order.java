package com.school.bookstore.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Table(name = "orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User user;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderItem> orderItems;

    @Enumerated(EnumType.STRING)
    OrderStatus orderStatus;

    @Column(name = "created_at")
    LocalDateTime createdAt;
}
