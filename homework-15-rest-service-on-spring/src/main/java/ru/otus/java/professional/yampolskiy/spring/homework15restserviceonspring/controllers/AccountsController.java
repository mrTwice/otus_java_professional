package ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.dtos.accounts.AccountDto;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.entities.Account;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.exceptions_handling.base.ResourceNotFoundException;
import ru.otus.java.professional.yampolskiy.spring.homework15restserviceonspring.services.AccountService;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountsController {
    private final AccountService accountService;
    private final Function<AccountDto, Account> AccountDtoToAccount = accountDto ->
            new Account(
                    accountDto.id(),
                    accountDto.accountNumber(),
                    accountDto.clientId(),
                    accountDto.balance(),
                    accountDto.isBlocked()
            );
    private final Function<Account, AccountDto> toAccountDto = account ->
            new AccountDto(
                    account.getId(),
                    account.getAccountNumber(),
                    account.getClientId(),
                    account.getBalance(),
                    account.isBlocked()
            );

    @GetMapping
    public List<AccountDto> getAllAccounts(@RequestHeader(name = "client-id") String clientId) {
        return accountService.findAll(clientId).stream().map(toAccountDto).collect(Collectors.toList());
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccount(@RequestHeader(name = "client-id") String clientId, @PathVariable String accountId) {
        Account account = accountService.findByClientIdAndId(clientId, accountId).orElseThrow(() -> new ResourceNotFoundException("Перевод не найден"));
        return toAccountDto.apply(account);
    }
}
