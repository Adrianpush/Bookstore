package com.school.bookstore.services;

import com.school.bookstore.exceptions.BookNotFoundException;
import com.school.bookstore.exceptions.OrderCreateException;
import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.entities.*;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.repositories.CustomerRepository;
import com.school.bookstore.repositories.OrderItemRepository;
import com.school.bookstore.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemService orderItemService;

    @Override
    public OrderDTO createOrder(Long customerId, OrderDTO shoppingCart) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        validateOrder(shoppingCart);

        Order order = new Order();
        order = orderRepository.save(order);
        addOrderItems(shoppingCart.getOrderItems(), order);
        order.setCustomer(customer);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);

        order = orderRepository.save(order);

        return convertToOrderDTO(order);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return null;
    }

    @Override
    public List<OrderDTO> getALlOrders() {
        return null;
    }

    @Override
    public List<OrderDTO> getAllOrdersByCustomer(Long customerId) {
        return null;
    }

    @Override
    public OrderDTO markOrderCompleted(Long orderId) {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) {

    }

    private OrderItem convertToOrderItemEntity(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .build();
    }

    private OrderDTO convertToOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderItems(order.getOrderItems().stream()
                        .map(orderItemService::convertoToOrderItemDTO)
                        .toList())
                .orderStatus(order.getOrderStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private void validateOrder(OrderDTO orderDTO) {
        orderDTO.getOrderItems()
                .forEach(orderItemService::validateOrderItemDTO);
    }

    private void addOrderItems(List<OrderItemDTO> orderItemDTOs, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItemDTOs.forEach(
                orderItemDTO -> orderItems.add(orderItemService.createOrderItem(orderItemDTO, order))
        );
        order.setOrderItems(orderItems);
    }
}
