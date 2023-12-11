package com.school.bookstore.services.interfaces;

import com.school.bookstore.models.dtos.EmailDTO;

public interface EmailService {

    void sendEmail(EmailDTO emailDTO);
}