package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.transfers;

public record TransferDto(String id, String clientId, String targetClientId, String sourceAccount, String targetAccount, String message,
                          int amount) {
}