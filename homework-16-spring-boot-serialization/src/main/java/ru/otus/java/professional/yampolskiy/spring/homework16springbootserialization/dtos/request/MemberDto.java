package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.dtos.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;



@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MemberDto {
    @JsonProperty("first")
    private String first;

    @JsonProperty("handle_id")
    private Long handleId;

    @JsonProperty("image_path")
    private String imagePath;

    @JsonProperty("last")
    private String last;

    @JsonProperty("middle")
    private String middle;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("service")
    private String service;

    @JsonProperty("thumb_path")
    private String thumbPath;
}
