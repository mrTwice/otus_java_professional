package ru.otus.java.professional.yampolskiy.hibernate.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    @OneToMany(mappedBy = "product")
    private List<PriceHistory> priceHistories;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", currentPrice=" + currentPrice +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(title, product.title) && Objects.equals(currentPrice, product.currentPrice) && Objects.equals(priceHistories, product.priceHistories) && Objects.equals(orderItems, product.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, currentPrice, priceHistories, orderItems);
    }
}
