package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseChatSessionDto {

    @JsonProperty("chat_identifier")
    private String chatIdentifier;

    @JsonProperty("members")
    private List<ResponseMemberDto> responseMemberDtos;

    @JsonProperty("messages")
    private List<ResponseMessageDto> responseMessageDtos;
}
