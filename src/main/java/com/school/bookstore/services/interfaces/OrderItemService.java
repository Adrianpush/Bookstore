package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;

public interface OrderItemService {

    void validateOrderItemDTO(OrderItemDTO orderItemDTO);

    OrderItem createOrderItem(OrderItemDTO orderItemDTO, Order order);

    void cancelOrderItem(OrderItem orderItem);

    OrderItemDTO convertoToOrderItemDTO(OrderItem orderItem);
}
