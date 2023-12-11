package com.school.bookstore.services.implementations;

import com.school.bookstore.models.dtos.EmailDTO;
import com.school.bookstore.services.interfaces.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendEmail(EmailDTO emailDTO) {
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
        javaMailSender.send(message);
        log.info("Email to {}, was sent.", emailDTO.getTo());
    }
}