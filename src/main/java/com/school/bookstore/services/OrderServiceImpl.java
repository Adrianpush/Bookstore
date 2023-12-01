package com.school.bookstore.services;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;
import com.school.bookstore.models.entities.OrderStatus;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.repositories.CustomerRepository;
import com.school.bookstore.repositories.OrderItemRepository;
import com.school.bookstore.repositories.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderDTO createOrder(Long customerId, List<OrderItemDTO> shoppingCart) {

        Order order = new Order();
        Long orderId = orderRepository.save(order).getId();
        order = new Order();
        order.setId(orderId);
        order.setCustomer(customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found")));
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = shoppingCart.stream()
                .filter(this::validateOrder)
                .map(orderItemDTO -> orderItemRepository.save(
                        OrderItem.builder()
                                .book(bookRepository.findById(orderItemDTO.getBookId()).get())
                                .quantity(orderItemDTO.getQuantity())
                                .order(orderRepository.findById(orderId).get())
                                .build()
                        ))
                .toList();

        order.setOrderItems(items);
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order = orderRepository.save(order);

        return OrderDTO.builder()
                .id(order.getId())
                .orderItems(order.getOrderItems().stream()
                        .map(orderItem -> OrderItemDTO.builder()
                                .id(orderItem.getId())
                                .bookId(orderItem.getBook().getId())
                                .quantity(orderItem.getQuantity())
                                .build())
                        .toList())
                .orderStatus(order.getOrderStatus())
                .build();
    }

    private boolean validateOrder(OrderItemDTO orderItemDTO) {
        Book book = bookRepository.findById(orderItemDTO.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
        return book.getCopiesAvailable() >= orderItemDTO.getQuantity();
    }

    private OrderItem convertToOrderItemEntity(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .build();
    }
}
