package ru.otus.java.professional.yampolskiy.jpql.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "address")
    @ToString.Exclude
    private Client client;

    @Column(nullable = false)
    private String street;

    public Address(String street) {
        this.street = street;
    }

    public Address(Client client, String street) {
        this(street);
        setClient(client);
    }

    public void setClient(Client client) {
        if (client != null && client.getAddress() != this) {
            this.client = client;
            client.setAddress(this);
        }
    }
}

