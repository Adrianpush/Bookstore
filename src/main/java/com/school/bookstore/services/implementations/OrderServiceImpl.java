package com.school.bookstore.services.implementations;

import com.school.bookstore.exceptions.order.OrderNotFoundException;
import com.school.bookstore.exceptions.users.AuthentificationException;
import com.school.bookstore.exceptions.users.UserNotFoundException;
import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.dtos.OrderSummaryDTO;
import com.school.bookstore.models.entities.Book;
import com.school.bookstore.models.entities.Order;
import com.school.bookstore.models.entities.OrderItem;
import com.school.bookstore.models.entities.User;
import com.school.bookstore.models.enums.OrderStatus;
import com.school.bookstore.models.enums.Role;
import com.school.bookstore.repositories.OrderRepository;
import com.school.bookstore.repositories.UserRepository;
import com.school.bookstore.services.interfaces.EmailService;
import com.school.bookstore.services.interfaces.OrderItemService;
import com.school.bookstore.services.interfaces.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String CUSTOMER_NOT_FOUND = "Customer %s not found.";
    private static final String ORDER_NOT_FOUND = "Order %s not found.";
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderItemService orderItemService;
    private final EmailService emailService;

    @Override
    public OrderDTO createOrder(String customerEmail, OrderDTO shoppingCart) {

        User user = userRepository.findByEmail(customerEmail)
                .orElseThrow(() -> new UserNotFoundException(CUSTOMER_NOT_FOUND.formatted(customerEmail)));
        validateOrder(shoppingCart);

        Order order = getOrder(shoppingCart, user);
        sendOrderConfirmation(user, order);

        return convertToOrderDTO(order);
    }

    @Override
    public OrderDTO getOrderById(String email, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND.formatted(orderId)));
        User user = order.getUser();
        validateRequest(email, user);
        return convertToOrderDTO(order);
    }

    @Override
    public List<OrderDTO> getALlOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderDTO)
                .toList();
    }

    @Override
    public List<OrderDTO> getAllOrdersByCustomer(String email, Long customerId) {
        User user;
        if (customerId == null) {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UserNotFoundException(CUSTOMER_NOT_FOUND.formatted(email)));
        } else {
            user = userRepository.findById(customerId)
                    .orElseThrow(() -> new UserNotFoundException("Customer not found"));
            validateRequest(email, user);
        }

        return orderRepository.findAllByUser(user).stream()
                .map(this::convertToOrderDTO)
                .toList();
    }

    @Override
    public OrderDTO markOrderCompleted(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND.formatted(orderId)));
        order.setOrderStatus(OrderStatus.COMPLETED);
        order = orderRepository.save(order);
        return convertToOrderDTO(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND.formatted(orderId)));
        order.getOrderItems().forEach(orderItemService::cancelOrderItem);
        order.setOrderStatus(OrderStatus.CANCELED);
        orderRepository.save(order);
    }

    @Override
    public List<String> getBooksBought(String requesterEmail) {
        User user = userRepository.findByEmail(requesterEmail)
                .orElseThrow(() -> new UserNotFoundException(CUSTOMER_NOT_FOUND.formatted(requesterEmail)));

        List<String> booksTitles = new ArrayList<>();
        orderRepository.findAllByUser(user).stream()
                .map(Order::getOrderItems)
                .flatMap(Collection::stream)
                .map(OrderItem::getBook)
                .distinct()
                .forEach(book -> booksTitles.add(book.getTitle()));

        return booksTitles;
    }

    @Override
    public boolean isBookPresentInOrders(Book book) {
        return orderRepository.existsBookInOrderItems(book);
    }

    @NotNull
    private Order getOrder(OrderDTO shoppingCart, User user) {
        Order order = new Order();
        order.setOrderItems(createOrderItems(shoppingCart.getOrderItems(), order));
        order.setUser(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);
        order = orderRepository.save(order);
        return order;
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

    private void sendOrderConfirmation(User user, Order order) {
        OrderSummaryDTO orderSummaryDTO = OrderSummaryDTO.builder()
                .recipientName(user.getFullName())
                .recipientEmail(user.getEmail())
                .books(order.getOrderItems().stream()
                        .map(orderItem -> orderItem.getBook().getTitle() + " x" + orderItem.getQuantity())
                        .toList())
                .build();
        emailService.sendOrderConfirmation(orderSummaryDTO);
    }

    private void validateRequest(String email, User user) {
        if (user.getRole() == Role.ROLE_USER && !user.getEmail().equals(email)) {
            throw new AuthentificationException("Not allowed to access this resource.");
        }
    }
}
