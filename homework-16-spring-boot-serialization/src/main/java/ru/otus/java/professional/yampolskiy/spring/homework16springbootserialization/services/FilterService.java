package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.services;

import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request.ChatSession;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.responce.ResponseChatSession;

import java.util.List;

public interface FilterService {
    List<ResponseChatSession> filter(List<ChatSession> chatSession, String belongNumber);
}
