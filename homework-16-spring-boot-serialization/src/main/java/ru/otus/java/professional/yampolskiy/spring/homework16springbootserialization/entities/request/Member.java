package ru.otus.java.professional.yampolskiy.spring.homework16springbootserialization.entities.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private String firstName;

    private Long handleId;

    private String imagePath;

    private String last;

    private String middle;

    private String phoneNumber;

    private String service;

    private String thumbPath;
}
