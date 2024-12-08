package ru.otus.java.professional.yampolskiy.hibernate.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    private Integer quantity;
    private BigDecimal priceAtOrdering;

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", order=" + order +
                ", product=" + product +
                ", quantity=" + quantity +
                ", priceAtOrdering=" + priceAtOrdering +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(order, orderItem.order) && Objects.equals(product, orderItem.product) && Objects.equals(quantity, orderItem.quantity) && Objects.equals(priceAtOrdering, orderItem.priceAtOrdering);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, product, quantity, priceAtOrdering);
    }
}
