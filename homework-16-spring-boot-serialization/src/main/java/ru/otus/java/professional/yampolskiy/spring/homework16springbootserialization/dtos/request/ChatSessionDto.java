package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatSessionDto {
    @JsonProperty("chat_id")
    private Long chatId;

    @JsonProperty("chat_identifier")
    private String chatIdentifier;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("is_deleted")
    private Integer isDeleted;

    @JsonProperty("members")
    private List<MemberDto> members;

    @JsonProperty("messages")
    private List<MessageDto> messages;
}
