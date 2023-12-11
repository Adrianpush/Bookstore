package com.school.bookstore.services;

import com.school.bookstore.exceptions.book.BookNotFoundException;
import com.school.bookstore.exceptions.order.OrderCreateException;
import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;
import com.school.bookstore.repositories.BookRepository;
import com.school.bookstore.services.interfaces.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final BookRepository bookRepository;

    @Override
    public void validateOrderItemDTO(OrderItemDTO orderItemDTO) {
        if (bookRepository.findById(orderItemDTO.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book with id " + orderItemDTO.getBookId() + "not found."))
                .getCopiesAvailable() < orderItemDTO.getQuantity()) {
            throw new OrderCreateException("Insufficient quantity for book " + orderItemDTO.getBookId());
        }
    }

    @Override
    public OrderItem createOrderItem(OrderItemDTO orderItemDTO, Order order) {
        Book book = bookRepository.findById(orderItemDTO.getBookId())
                .orElseThrow(() -> new BookNotFoundException("Book not found"));

        OrderItem orderItem = OrderItem.builder()
                .book(book)
                .quantity(orderItemDTO.getQuantity())
                .order(order)
                .build();

        book.setCopiesAvailable(book.getCopiesAvailable() - orderItem.getQuantity());
        bookRepository.save(book);

        return orderItem;
    }

    @Override
    public void cancelOrderItem(OrderItem orderItem) {
        Book book = orderItem.getBook();
        book.setCopiesAvailable(book.getCopiesAvailable() + orderItem.getQuantity());
        bookRepository.save(book);
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
