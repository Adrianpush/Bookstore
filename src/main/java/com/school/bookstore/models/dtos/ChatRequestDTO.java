package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.Message;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ChatRequestDTO {

    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;

    public ChatRequestDTO(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }
}