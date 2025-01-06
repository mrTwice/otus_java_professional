package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.ExecuteTransferDtoRq;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Transfer;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.ValidationException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.ValidationFieldError;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.repositories.TransfersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransfersService {
    private final TransfersRepository transfersRepository;

    public Optional<Transfer> getTransferById(String id, String clientId) {
        return transfersRepository.findByIdAndClientId(id, clientId);
    }

    public List<Transfer> getAllTransfers(String clientId) {
        return transfersRepository.findAllByClientId(clientId);
    }

    public void execute(String clientId, ExecuteTransferDtoRq executeTransferDtoRq) {
        validateExecuteTransferDtoRq(executeTransferDtoRq);

    }

    private void validateExecuteTransferDtoRq(ExecuteTransferDtoRq executeTransferDtoRq) {
        List<ValidationFieldError> errors = new ArrayList<>();
        if (executeTransferDtoRq.sourceAccount().length() != 12) {
            errors.add(new ValidationFieldError("sourceAccount", "Длина поля счет отправителя должна составлять 12 символов"));
        }
        if (executeTransferDtoRq.targetAccount().length() != 12) {
            errors.add(new ValidationFieldError("targetAccount", "Длина поля счет получателя должна составлять 12 символов"));
        }
        if (executeTransferDtoRq.amount() <= 0) {
            errors.add(new ValidationFieldError("amount", "Сумма перевода должна быть больше 0"));
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("EXECUTE_TRANSFER_VALIDATION_ERROR", "Проблемы заполнения полей перевода", errors);
        }
    }
}
