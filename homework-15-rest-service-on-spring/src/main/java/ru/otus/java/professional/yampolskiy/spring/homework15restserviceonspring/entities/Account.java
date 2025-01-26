package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "balance", nullable = false)
    private int balance;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;
}
