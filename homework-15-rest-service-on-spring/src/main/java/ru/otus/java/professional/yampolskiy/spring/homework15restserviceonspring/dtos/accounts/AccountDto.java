package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.accounts;

public record AccountDto(String id, String accountNumber, String clientId, int balance, boolean isBlocked) {
}
