package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.OrderSummaryDTO;

public interface EmailService {

    void sendWelcomeEmail(String fullName, String customerEmail);

    void sendOrderConfirmation(OrderSummaryDTO orderSummaryDTO);
}