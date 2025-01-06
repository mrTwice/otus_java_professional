package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    List<Account> findAllByClientId(String clientId);
    Optional<Account> findByClientIdAndId(String clientId, String id);
}
