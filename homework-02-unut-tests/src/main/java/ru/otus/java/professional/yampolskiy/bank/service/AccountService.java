package ru.otus.java.professional.yampolskiy.bank.service;

import ru.otus.java.professional.yampolskiy.bank.entity.Account;
import ru.otus.java.professional.yampolskiy.bank.entity.Agreement;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account addAccount(Agreement agreement, String accountNumber, Integer type, BigDecimal amount);

    List<Account> getAccounts();

    List<Account> getAccounts(Agreement agreement);

    boolean makeTransfer(Long sourceAccountId, Long destinationAccountId, BigDecimal sum);

    boolean charge(Long accountId, BigDecimal chargeAmount);
}
