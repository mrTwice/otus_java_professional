package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatSession {
    private Long chatId;

    private String chatIdentifier;

    private String displayName;

    private Integer isDeleted;

    private List<Member> members;

    private List<Message> messages;
}
