package com.school.bookstore.services;

import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;

public interface OrderItemService {

    void validateOrderItemDTO(OrderItemDTO orderItemDTO);

    OrderItem createOrderItem(OrderItemDTO orderItemDTO, Order order);

    OrderItemDTO convertoToOrderItemDTO(OrderItem orderItem);
}
