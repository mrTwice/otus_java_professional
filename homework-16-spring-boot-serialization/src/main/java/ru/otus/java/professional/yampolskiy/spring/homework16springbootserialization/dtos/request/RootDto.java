package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RootDto {
    @JsonProperty("chat_sessions")
    private List<ChatSessionDto> chat_sessions;
}
