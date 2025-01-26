package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.transfers;

public record ExecuteTransferDtoRq(String targetClientId, String sourceAccount, String targetAccount, String message, int amount) {
}