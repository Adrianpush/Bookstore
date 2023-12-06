package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Customer;
import com.school.bookstore.models.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByCustomer(Customer customer);
}
