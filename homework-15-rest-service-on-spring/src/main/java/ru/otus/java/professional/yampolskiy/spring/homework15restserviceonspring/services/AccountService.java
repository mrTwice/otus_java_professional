package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.services;

import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Account;

import java.util.List;
import java.util.Optional;


public interface AccountService {

    List<Account> findAll(String clientId);

    Optional<Account> findByClientIdAndAccountNumber(String clientId, String accountNumber);

    void save(Account account);
}
