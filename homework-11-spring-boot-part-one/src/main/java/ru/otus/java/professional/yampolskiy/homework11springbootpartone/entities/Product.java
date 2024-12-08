package ru.otus.java.professional.yampolskiy.homework11springbootpartone.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    private Long id;
    private String title;
    private double price;
}
