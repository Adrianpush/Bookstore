package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.entities.Book;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(String email, OrderDTO shoppingCart);

    OrderDTO getOrderById(String email, Long orderId);

    List<OrderDTO> getALlOrders();

    List<OrderDTO> getAllOrdersByCustomer(String email, Long customerId);

    OrderDTO markOrderCompleted(Long orderId);

    void cancelOrder(String requesterEmail, Long orderId);

    List<String> getBooksBought(String requesterEmail);

    boolean isBookPresentInOrders(Book book);
}