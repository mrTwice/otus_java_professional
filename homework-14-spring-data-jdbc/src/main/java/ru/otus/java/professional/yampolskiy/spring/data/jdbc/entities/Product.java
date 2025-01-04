package ru.otus.java.professional.yampolskiy.spring.data.jdbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Product {
    @Id
    private Long id;
    private String name;
    private double price;
}
