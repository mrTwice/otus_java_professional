package ru.otus.java.professional.yampolskiy.jpql.entities;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Entity
@Table(name = "phones")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Client client;

    @Column(nullable = false)
    private String number;

    public Phone(String number) {
        this.number = number;
    }

    public Phone(String number, Client client) {
        this(number);
        setClient(client);
    }

    public void setClient(Client client) {
        if (this.client != null) {
            this.client.removePhone(this);
        }
        this.client = client;
        if (client != null && !client.getPhones().contains(this)) {
            client.addPhone(this);
        }
    }
}

