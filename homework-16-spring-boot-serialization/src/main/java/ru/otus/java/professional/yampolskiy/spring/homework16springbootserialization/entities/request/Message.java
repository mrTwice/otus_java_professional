package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Long rowId;

    private String attributedBody;

    private String belongNumber;

    private String date;

    private String dateRead;

    private UUID guid;

    private Long handleId;

    private Integer hasDdResults;

    private Integer isDeleted;

    private Integer isFromMe;

    private LocalDateTime sendDate;

    private Integer sendStatus;

    private String service;

    private String text;

}
