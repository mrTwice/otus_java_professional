package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.transfers;

import java.util.List;

public record TransfersPageDto(List<TransferDto> entries) {
}