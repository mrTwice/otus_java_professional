package ru.otus.java.professional.yampolskiy.jpql.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude
    private Address address;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Phone> phones = new ArrayList<>();

    public Client(String name, Address address) {
        this.name = name;
        this.address = address;
        address.setClient(this);
    }

    public Client(String name, Address address, List<Phone> phones) {
        this(name, address);
        if (phones != null) {
            phones.forEach(this::addPhone);
        }
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
        phone.setClient(this);
    }

    public void removePhone(Phone phone) {
        phones.remove(phone);
        phone.setClient(null);
    }

    public void changeAddress(Address address) {
        this.address = address;
        address.setClient(this);
    }
}

