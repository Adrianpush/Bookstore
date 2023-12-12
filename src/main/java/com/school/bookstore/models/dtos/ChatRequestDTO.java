package com.school.bookstore.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDTO implements Serializable {

    private String model;
    private List<MessageDTO> messages;

    public ChatRequestDTO(String model, String prompt) {
        this.model = model;
        this.messages = new ArrayList<>();
        this.messages.add(new MessageDTO("user", prompt));
    }
}