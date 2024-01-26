package com.school.bookstore.services.implementations;

import com.school.bookstore.models.dtos.EmailDTO;
import com.school.bookstore.models.dtos.OrderSummaryDTO;
import com.school.bookstore.services.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;
    private final TemplateEngine emailTemplateEngine;
    @Value("${spring.mail.from}")
    private String hostEmail;

    @Override
    public void sendWelcomeEmail(String fullName, String customerEmail) {
        Context context = new Context();
        context.setVariable("userName", fullName);
        String emailContent = emailTemplateEngine.process("welcome", context);

        EmailDTO emailDTO = EmailDTO.builder()
                .from(hostEmail)
                .to(customerEmail)
                .subject("Welcome to PageFlip")
                .body(emailContent)
                .build();

        sendEmail(emailDTO);
    }

    @Override
    public void sendOrderConfirmation(OrderSummaryDTO orderSummaryDTO) {
        Context context = new Context();
        context.setVariable("userName", orderSummaryDTO.getRecipientName());
        context.setVariable("books", orderSummaryDTO.getBooks());
        String emailContent = emailTemplateEngine.process("order-confirmation", context);

        EmailDTO emailDTO = EmailDTO.builder()
                .from(hostEmail)
                .to(orderSummaryDTO.getRecipientEmail())
                .subject("Your order from PageFlip")
                .body(emailContent)
                .build();

        sendEmail(emailDTO);
    }

    private void sendEmail(EmailDTO emailDTO) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setSubject(emailDTO.getSubject());
            helper.setFrom(emailDTO.getFrom());
            helper.setTo(emailDTO.getTo());
            helper.setText(emailDTO.getBody(), true);
        } catch (MessagingException e) {
            log.error("Email was not sent to {}, because:  {}", emailDTO.getTo(), e.getMessage());
        }
        //javaMailSender.send(message);
        log.info("Email to {}, was sent.", emailDTO.getTo());
    }
}