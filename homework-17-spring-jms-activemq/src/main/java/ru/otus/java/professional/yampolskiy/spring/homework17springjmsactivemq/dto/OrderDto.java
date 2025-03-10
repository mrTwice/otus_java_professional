package ru.otus.java.professional.yampolskiy.spring.homework17springjmsactivemq.dto;

import lombok.*;

@ToString
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private String title;
    private int value;

}
