package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.services;

import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.transfers.ExecuteTransferDtoRq;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransfersService {

    Optional<Transfer> getTransferById(String id, String clientId);

    List<Transfer> getAllTransfers(String clientId);

    void execute(String clientId, ExecuteTransferDtoRq executeTransferDtoRq);
}
