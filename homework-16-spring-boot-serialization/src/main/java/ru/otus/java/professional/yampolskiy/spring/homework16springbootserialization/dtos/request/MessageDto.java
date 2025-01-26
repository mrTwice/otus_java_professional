package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageDto {
    @JsonProperty("ROWID")
    private Long rowId;

    @JsonProperty("attributedBody")
    private String attributedBody;

    @JsonProperty("belong_number")
    private String belongNumber;

    @JsonProperty("date")
    private String date;

    @JsonProperty("date_read")
    private String dateRead;

    @JsonProperty("guid")
    private UUID guid;

    @JsonProperty("handle_id")
    private Long handleId;

    @JsonProperty("has_dd_results")
    private Integer hasDdResults;

    @JsonProperty("is_deleted")
    private Integer isDeleted;

    @JsonProperty("is_from_me")
    private Integer isFromMe;

    @JsonProperty("send_date")
    @JsonFormat(pattern = "MM-dd-yyyy HH:mm:ss")
    private LocalDateTime sendDate;

    @JsonProperty("send_status")
    private Integer sendStatus;

    @JsonProperty("service")
    private String service;

    @JsonProperty("text")
    private String text;

}
