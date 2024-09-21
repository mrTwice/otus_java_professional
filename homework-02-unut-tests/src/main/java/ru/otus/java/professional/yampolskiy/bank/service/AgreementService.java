package ru.otus.java.professional.yampolskiy.bank.service;

import ru.otus.java.professional.yampolskiy.bank.entity.Agreement;

import java.util.Optional;

public interface AgreementService {
    Agreement addAgreement(String name);

    Optional<Agreement> findByName(String name);
}
