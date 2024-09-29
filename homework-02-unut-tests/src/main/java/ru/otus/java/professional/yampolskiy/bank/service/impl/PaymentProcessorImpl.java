package ru.otus.java.professional.yampolskiy.bank.service.impl;

import ru.otus.java.professional.yampolskiy.bank.entity.Account;
import ru.otus.java.professional.yampolskiy.bank.entity.Agreement;
import ru.otus.java.professional.yampolskiy.bank.service.AccountService;
import ru.otus.java.professional.yampolskiy.bank.service.PaymentProcessor;
import ru.otus.java.professional.yampolskiy.bank.service.exception.AccountException;

import java.math.BigDecimal;

public class PaymentProcessorImpl implements PaymentProcessor {
    private AccountService accountService;

    public PaymentProcessorImpl(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean makeTransfer(Agreement source, Agreement destination, int sourceType,
                                int destinationType, BigDecimal amount) {

        Account sourceAccount = findAccount(source, sourceType);
        Account destinationAccount = findAccount(destination, destinationType);

        return accountService.makeTransfer(sourceAccount.getId(), destinationAccount.getId(), amount);
    }

    @Override
    public boolean makeTransferWithComission(Agreement source, Agreement destination,
                                             int sourceType, int destinationType,
                                             BigDecimal amount,
                                             BigDecimal comissionPercent) {

        Account sourceAccount = findAccount(source, sourceType);
        Account destinationAccount = findAccount(destination, destinationType);

        accountService.charge(sourceAccount.getId(), amount.negate().multiply(comissionPercent));

        return accountService.makeTransfer(sourceAccount.getId(), destinationAccount.getId(), amount);
    }

    Account findAccount(Agreement agreement, int accountType) {
        return accountService.getAccounts(agreement).stream()
                .filter(account -> account.getType() == accountType)
                .findAny()
                .orElseThrow(() -> new AccountException("Account not found"));
    }
}
