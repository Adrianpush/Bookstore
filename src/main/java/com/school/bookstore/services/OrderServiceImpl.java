package com.school.bookstore.services;

import com.school.bookstore.exceptions.CustomerNotFoundException;
import com.school.bookstore.exceptions.OrderNotFoundException;
import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.entities.*;
import com.school.bookstore.repositories.UserRepository;
import com.school.bookstore.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemService orderItemService;

    @Override
    public OrderDTO createOrder(Long customerId, OrderDTO shoppingCart) {

        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        validateOrder(shoppingCart);

        Order order = new Order();
        order.setOrderItems(createOrderItems(shoppingCart.getOrderItems(), order));
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order = orderRepository.save(order);

        return convertToOrderDTO(order);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return convertToOrderDTO(orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found")));
    }

    @Override
    public List<OrderDTO> getALlOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getAllOrdersByCustomer(Long customerId) {
        User user = userRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        return orderRepository.findAllByUser(user).stream()
                .map(this::convertToOrderDTO)
                .toList();
    }

    @Override
    public OrderDTO markOrderCompleted(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.setOrderStatus(OrderStatus.COMPLETED);

        return convertToOrderDTO(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        order.getOrderItems().forEach(orderItemService::cancelOrderItem);
        order.setOrderStatus(OrderStatus.CANCELED);
    }

    private OrderDTO convertToOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderItems(order.getOrderItems().stream()
                        .map(orderItemService::convertoToOrderItemDTO)
                        .toList())
                .customerId(order.getUser().getId())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private void validateOrder(OrderDTO orderDTO) {
        orderDTO.getOrderItems()
                .forEach(orderItemService::validateOrderItemDTO);
    }

    private List<OrderItem> createOrderItems(List<OrderItemDTO> orderItemDTOs, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();

        orderItemDTOs.forEach(
                orderItemDTO -> orderItems.add(orderItemService.createOrderItem(orderItemDTO, order))
        );
        return orderItems;
    }
}
