package com.school.bookstore.services;

import com.school.bookstore.exceptions.BookNotFoundException;
import com.school.bookstore.exceptions.OrderCreateException;
import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.repositories.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService{

    private final OrderItemRepository orderItemRepository;
    private final BookRepository bookRepository;


    @Override
    public void validateOrderItemDTO(OrderItemDTO orderItemDTO) {
        if (bookRepository.findById(orderItemDTO.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id " + orderItemDTO.getBookId() + "not found."))
                .getCopiesAvailable() < orderItemDTO.getQuantity()) {
            throw new OrderCreateException("Insuficient quantity for book " + orderItemDTO.getBookId());
        }
    }

    @Override
    public OrderItem createOrderItem(OrderItemDTO orderItemDTO, Order order) {
        OrderItem orderItem = OrderItem.builder()
                .book(bookRepository.findById(orderItemDTO.getBookId()).get())
                .quantity(orderItemDTO.getQuantity())
                .order(order)
                .build();

        return orderItemRepository.save(orderItem);
    }

    @Override
    public OrderItemDTO convertoToOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .bookId(orderItem.getBook().getId())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
