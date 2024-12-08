package ru.otus.java.professional.yampolskiy.hibernate.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", client=" + client +
                ", orderDate=" + orderDate +
                ", orderItems=" + orderItems +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(client, order.client) && Objects.equals(orderDate, order.orderDate) && Objects.equals(orderItems, order.orderItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, orderDate, orderItems);
    }
}
