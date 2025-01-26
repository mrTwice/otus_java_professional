package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMemberDto {

    @JsonProperty("last")
    private String last;

}
