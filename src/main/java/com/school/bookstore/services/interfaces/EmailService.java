package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.EmailDTO;
import com.school.bookstore.models.dtos.OrderItemDTO;
import com.school.bookstore.models.dtos.OrderSummaryDTO;

import java.util.List;

public interface EmailService {

    void sendWelcomeEmail(String fullName, String customerEmail);

    void sendOrderConfirmation(OrderSummaryDTO orderSummaryDTO);
}