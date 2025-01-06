package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.transfers.ExecuteTransferDtoRq;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Account;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Transfer;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base.BusinessLogicException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base.ResourceNotFoundException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.validations.ValidationException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.validations.ValidationFieldError;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.repositories.TransfersRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransfersService {
    private final TransfersRepository transfersRepository;
    private final AccountService accountService;

    public Optional<Transfer> getTransferById(String id, String clientId) {
        return transfersRepository.findByIdAndClientId(id, clientId);
    }

    public List<Transfer> getAllTransfers(String clientId) {
        return transfersRepository.findTransfersByClientId(clientId);
    }

    @Transactional
    public void execute(String clientId, ExecuteTransferDtoRq executeTransferDtoRq) {
        validateExecuteTransferDtoRq(executeTransferDtoRq);

        Account sourceAccount = accountService.findByClientIdAndAccountNumber(clientId, executeTransferDtoRq.sourceAccount())
                .orElseThrow(() -> new ResourceNotFoundException("Исходный счет не найден"));

        Account targetAccount = accountService.findByClientIdAndAccountNumber(executeTransferDtoRq.targetClientId(), executeTransferDtoRq.targetAccount())
                .orElseThrow(() -> new ResourceNotFoundException("Целевой счет не найден"));

        validateAccounts(sourceAccount, targetAccount, executeTransferDtoRq.amount());

        sourceAccount.setBalance(sourceAccount.getBalance() - executeTransferDtoRq.amount());
        targetAccount.setBalance(targetAccount.getBalance() + executeTransferDtoRq.amount());

        accountService.save(sourceAccount);
        accountService.save(targetAccount);

        Transfer transfer = new Transfer(
                UUID.randomUUID().toString(),
                clientId,
                executeTransferDtoRq.targetClientId(),
                executeTransferDtoRq.sourceAccount(),
                executeTransferDtoRq.targetAccount(),
                executeTransferDtoRq.message(),
                executeTransferDtoRq.amount()
        );
        transfersRepository.save(transfer);
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

    private void validateAccounts(Account sourceAccount, Account targetAccount, int amount) {
        if (sourceAccount.isBlocked()) {
            throw new BusinessLogicException("Счет списания заблокирован.", "EXECUTE_TRANSFER_SOURCE_ACCOUNT_BLOCKED_ERROR");
        }

        if (targetAccount.isBlocked()) {
            throw new BusinessLogicException("Счет зачисления заблокирован.", "EXECUTE_TRANSFER_TARGET_ACCOUNT_BLOCKED_ERROR");
        }

        if (sourceAccount.getBalance() < amount) {
            throw new BusinessLogicException("Счет зачисления заблокирован.", "EXECUTE_TRANSFER_NOT_ENOUGH_AMOUNT_ERROR");
        }
    }

}
