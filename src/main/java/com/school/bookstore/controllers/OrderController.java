package com.school.bookstore.controllers;

import com.school.bookstore.models.dtos.OrderDTO;
import com.school.bookstore.services.interfaces.JwtService;
import com.school.bookstore.services.interfaces.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final JwtService jwtService;

    @Secured("ROLE_USER")
    @PostMapping()
    public ResponseEntity<OrderDTO> createOrder(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @Valid @RequestBody OrderDTO shoppingCart) {
        return ResponseEntity.ok(orderService.createOrder(jwtService.extractUserName(authorizationHeader.substring(7)), shoppingCart));
    }

    @Secured("ROLE_STAFF")
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.getALlOrders());
    }

    @Secured({"ROLE_USER", "ROLE_STAFF"})
    @GetMapping("customers/{customerId}")
    public ResponseEntity<List<OrderDTO>> getAllOrdersByCustomer(@RequestHeader(name = "Authorization") String authorizationHeader,
                                                                 @PathVariable(required = false) Long customerId) {
        return ResponseEntity.ok(orderService.getAllOrdersByCustomer(
                jwtService.extractUserName(authorizationHeader.substring(7)), customerId)
        );
    }

    @Secured({"ROLE_USER", "ROLE_STAFF"})
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderByOrderId(@RequestHeader(name = "Authorization") String authorizationHeader,
                                                      @PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(jwtService.extractUserName(authorizationHeader.substring(7)), orderId));
    }

    @Secured({"ROLE_STAFF"})
    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderDTO> markOrderComplete(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.markOrderCompleted(orderId));
    }

    @Secured({"ROLE_USER"})
    @DeleteMapping("/{orderId}")
    public HttpStatus cancelOrder(
            @RequestHeader(name = "Authorization") String authorizationHeader,
            @PathVariable Long orderId) {
        orderService.cancelOrder(jwtService.extractUserName(authorizationHeader.substring(7)), orderId);
        return HttpStatus.NO_CONTENT;
    }
}
