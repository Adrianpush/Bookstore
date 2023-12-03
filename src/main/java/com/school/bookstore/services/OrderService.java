package com.school.bookstore.services;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.dtos.OrderItemDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(Long customer, OrderDTO shoppingCart);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getALlOrders();

    List<OrderDTO> getAllOrdersByCustomer(Long customerId);

    OrderDTO markOrderCompleted(Long orderId);

    void deleteOrder(Long orderId);
}