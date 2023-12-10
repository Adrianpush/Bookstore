package com.school.bookstore.models.dtos;

import com.school.bookstore.models.entities.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChatResponseDTO {

    private List<Choice> choices;

    @Data
    @AllArgsConstructor
    public static class Choice {

        private int index;
        private Message message;
    }
}