package com.school.bookstore.services;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.dtos.OrderItemDTO;

import java.util.List;

public interface OrderService {

    OrderDTO createOrder(Long customer, List<OrderItemDTO> shoppingCart);
}
