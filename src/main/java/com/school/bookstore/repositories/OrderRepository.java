package com.school.bookstore.repositories;

import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);

    @Query("SELECT CASE WHEN COUNT(oi) > 0 THEN true ELSE false END FROM Order o JOIN o.orderItems oi WHERE oi.book = :book")
    boolean existsBookInOrderItems(@Param("book") Book book);
}