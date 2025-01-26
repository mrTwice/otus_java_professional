package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.request.ChatSessionDto;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.request.RootDto;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.response.ResponseChatSessionDto;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.response.ResponseMemberDto;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.response.ResponseMessageDto;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request.ChatSession;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request.Member;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request.Message;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.responce.ResponseChatSession;
import ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.services.FilterService;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/filter")
@RequiredArgsConstructor
public class FilterController {

    private final FilterService filterService;

    private static final Function<ChatSessionDto, ChatSession> chatSessionDtoToEntity = chatSessionDto -> {
        ChatSession chatSession = new ChatSession();
        chatSession.setChatId(chatSessionDto.getChatId());
        chatSession.setChatIdentifier(chatSessionDto.getChatIdentifier());
        chatSession.setDisplayName(chatSessionDto.getDisplayName());
        chatSession.setIsDeleted(chatSessionDto.getIsDeleted());

        List<Member> members = chatSessionDto.getMembers().stream().map(memberDto -> {
            Member member = new Member();
            member.setFirstName(memberDto.getFirst());
            member.setHandleId(memberDto.getHandleId());
            member.setImagePath(memberDto.getImagePath());
            member.setLast(memberDto.getLast());
            member.setMiddle(memberDto.getMiddle());
            member.setPhoneNumber(memberDto.getPhoneNumber());
            member.setService(memberDto.getService());
            member.setThumbPath(memberDto.getThumbPath());
            return member;
        }).collect(Collectors.toList());

        List<Message> messages = chatSessionDto.getMessages().stream().map(messageDto -> {
            Message message = new Message();
            message.setRowId(messageDto.getRowId());
            message.setAttributedBody(messageDto.getAttributedBody());
            message.setBelongNumber(messageDto.getBelongNumber());
            message.setDate(messageDto.getDate());
            message.setDateRead(messageDto.getDateRead());
            message.setGuid(messageDto.getGuid());
            message.setHandleId(messageDto.getHandleId());
            message.setHasDdResults(messageDto.getHasDdResults());
            message.setIsDeleted(messageDto.getIsDeleted());
            message.setIsFromMe(messageDto.getIsFromMe());
            message.setSendDate(messageDto.getSendDate());
            message.setSendStatus(messageDto.getSendStatus());
            message.setService(messageDto.getService());
            message.setText(messageDto.getText());
            return message;
        }).collect(Collectors.toList());

        chatSession.setMembers(members);
        chatSession.setMessages(messages);

        return chatSession;
    };

    private static final Function<ResponseChatSession, ResponseChatSessionDto> entityToResponseChatSession = entity ->
            new ResponseChatSessionDto(
                    entity.getChatIdentifier(),
                    entity.getResponseMembers() != null
                            ? entity.getResponseMembers().stream()
                            .map(member -> new ResponseMemberDto(member.getLast()))
                            .toList()
                            : Collections.emptyList(),
                    entity.getResponseMessages() != null
                            ? entity.getResponseMessages().stream()
                            .map(message -> new ResponseMessageDto(
                                    message.getBelongNumber(),
                                    message.getSendDate(),
                                    message.getText()
                            ))
                            .toList()
                            : Collections.emptyList()
            );

    @PostMapping
    public List<ResponseChatSessionDto> filter(@RequestBody RootDto rootDto, @RequestHeader("belong-number") String belongNumber) {
        List<ChatSession> chatSessions = rootDto.getChat_sessions().stream().map(chatSessionDtoToEntity).toList();
        List<ResponseChatSession> responseChatSessions = filterService.filter(chatSessions, belongNumber);
        return responseChatSessions.stream().map(entityToResponseChatSession).toList();
    }

}
