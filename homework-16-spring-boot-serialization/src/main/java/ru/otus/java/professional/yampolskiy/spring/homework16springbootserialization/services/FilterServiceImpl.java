package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.services;

import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request.ChatSession;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request.Message;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.responce.ResponseChatSession;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.responce.ResponseMember;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.responce.ResponseMessage;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FilterServiceImpl implements FilterService {

    private final Function<ChatSession, ResponseChatSession> chatSessionToResponseChatSession = session ->
            new ResponseChatSession(
                    session.getChatIdentifier(),
                    session.getMembers() != null
                            ? session.getMembers().stream()
                            .map(member -> new ResponseMember(member.getLast()))
                            .toList()
                            : Collections.emptyList(),
                    session.getMessages() != null
                            ? session.getMessages().stream()
                            .map(message -> new ResponseMessage(
                                    message.getBelongNumber(),
                                    message.getSendDate(),
                                    message.getText()))
                            .toList()
                            : Collections.emptyList()
            );

    public List<ResponseChatSession> filter(List<ChatSession> chatSessions, String belongNumber) {
        List<ChatSession> filteredChatSessions = chatSessions.stream()
                .filter(chatSession ->
                        chatSession.getMessages().stream()
                                .anyMatch(message -> belongNumber.equals(message.getBelongNumber()))
                )
                .map(chatSession -> {
                    List<Message> filteredMessages = chatSession.getMessages().stream()
                            .filter(message -> belongNumber.equals(message.getBelongNumber()))
                            .sorted((m1, m2) -> m1.getSendDate().compareTo(m2.getSendDate()))
                            .collect(Collectors.toList());
                    chatSession.setMessages(filteredMessages);
                    return chatSession;
                })
                .toList();
        return filteredChatSessions.stream().map(chatSessionToResponseChatSession).toList();
    }
}
