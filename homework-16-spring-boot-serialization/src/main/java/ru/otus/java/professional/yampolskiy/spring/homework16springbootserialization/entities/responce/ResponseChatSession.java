package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.responce;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseChatSession {

    private String chatIdentifier;

    private List<ResponseMember> responseMembers;

    private List<ResponseMessage> responseMessages;
}
