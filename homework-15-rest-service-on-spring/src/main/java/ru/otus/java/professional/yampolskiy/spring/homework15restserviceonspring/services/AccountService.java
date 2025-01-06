package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Account;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.repositories.AccountRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public List<Account> findAll(String clientId) {
        return accountRepository.findAllByClientId(clientId);
    }

    public Optional<Account> findByClientIdAndAccountNumber(String clientId, String accountNumber) {
        return accountRepository.findByClientIdAndAccountNumber(clientId, accountNumber);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }
}
