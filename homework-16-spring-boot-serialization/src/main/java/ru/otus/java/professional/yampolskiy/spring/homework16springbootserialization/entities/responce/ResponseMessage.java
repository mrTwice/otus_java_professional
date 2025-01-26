package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.responce;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseMessage {

    private String belongNumber;

    private LocalDateTime sendDate;

    private String text;

}
