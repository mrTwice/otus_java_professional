package ru.otus.java.professional.yampolskiy.bank.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.java.professional.yampolskiy.bank.dao.AccountDao;
import ru.otus.java.professional.yampolskiy.bank.entity.Account;
import ru.otus.java.professional.yampolskiy.bank.entity.Agreement;
import ru.otus.java.professional.yampolskiy.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    AccountDao accountDao;

    @InjectMocks
    AccountServiceImpl accountServiceImpl;

    private static Stream<? extends Arguments> provideParametersForAddAccountMethod() {
        Agreement agreement = new Agreement();
        agreement.setId(1L);
        return Stream.of(
                Arguments.of(agreement, "1", 0, new BigDecimal(100)),
                Arguments.of(agreement, "2", 0, new BigDecimal(0))
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForAddAccountMethod")
    //TODO: Возможно тут лучше передавать не объект типа Agreement, а просто его id
    void addAccount(Agreement agreement, String accountNumber, Integer type, BigDecimal amount) {
        Account expectedAccount = new Account();
        expectedAccount.setAgreementId(agreement.getId());
        expectedAccount.setNumber(accountNumber);
        expectedAccount.setType(type);
        expectedAccount.setAmount(amount);

        when(accountDao.save(any(Account.class))).thenReturn(expectedAccount);
        Account result = accountServiceImpl.addAccount(agreement, accountNumber, type, amount);

        verify(accountDao).save(argThat(account ->
                account.getAgreementId().equals(agreement.getId()) &&
                        account.getNumber().equals(accountNumber) &&
                        account.getType().equals(type) &&
                        account.getAmount().compareTo(amount) == 0
        ));
        assertEquals(expectedAccount, result);
    }

    private static Stream<Arguments> provideParametersForGetAccountsMethod() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setAmount(new BigDecimal(100));
        account1.setAgreementId(100L);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setAmount(new BigDecimal(200));
        account2.setAgreementId(200L);

        return Stream.of(
                Arguments.of(Arrays.asList(account1, account2), Arrays.asList(account1, account2)),
                Arguments.of(Collections.emptyList(), Collections.emptyList()),
                Arguments.of(null, Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForGetAccountsMethod")
    void getAccounts(List<Account> daoResult, List<Account> expectedResult) {
        when(accountDao.findAll()).thenReturn(daoResult);
        List<Account> result = accountServiceImpl.getAccounts();
        assertEquals(expectedResult, result);
        verify(accountDao).findAll();
    }


    private static Stream<Arguments> provideParametersForChargeMethod() {
        Account account1 = new Account();
        account1.setId(1L);
        account1.setAmount(new BigDecimal(100));
        account1.setAgreementId(100L);

        Account account2 = new Account();
        account2.setId(2L);
        account2.setAmount(new BigDecimal(50));
        account2.setAgreementId(200L);

        return Stream.of(
                Arguments.of(1L, new BigDecimal("100.00"), new BigDecimal("50.00"), new BigDecimal("50.00"), true, null),
                Arguments.of(2L, new BigDecimal("50.00"), new BigDecimal("50.00"), new BigDecimal("0.00"), true, null),
                Arguments.of(3L, null, new BigDecimal("50.00"), null, false, "No source account"),
                Arguments.of(4L, new BigDecimal("50.00"), new BigDecimal("60.00"), new BigDecimal("-10.00"), true, null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForChargeMethod")
    void testCharge(Long accountId, BigDecimal initialAmount, BigDecimal chargeAmount, BigDecimal expectedBalance, boolean expectedResult, String expectedExceptionMessage) {
        Optional<Account> accountFromDao = initialAmount != null
                ? Optional.of(createAccount(accountId, initialAmount))
                : Optional.empty();

        when(accountDao.findById(accountId)).thenReturn(accountFromDao);

        if (expectedExceptionMessage != null) {
            AccountException exception = assertThrows(AccountException.class, () -> {
                accountServiceImpl.charge(accountId, chargeAmount);
            });
            assertEquals(expectedExceptionMessage, exception.getMessage());
            verify(accountDao, never()).save(any(Account.class));
        } else {
            boolean result = accountServiceImpl.charge(accountId, chargeAmount);
            assertEquals(expectedResult, result);
            assertEquals(expectedBalance, accountFromDao.get().getAmount());
            verify(accountDao).save(accountFromDao.get());
        }
    }

    public static Stream<Arguments> provideParametersForTestGetAccounts() {
        Agreement agreement1 = new Agreement();
        agreement1.setId(1L);

        Agreement agreement2 = new Agreement();
        agreement2.setId(2L);

        List<Account> accountsForAgreement1 = List.of(
                createAccount(1L, new BigDecimal("100.00"), agreement1.getId()),
                createAccount(2L, new BigDecimal("200.00"), agreement1.getId())
        );

        List<Account> accountsForAgreement2 = List.of(
                createAccount(3L, new BigDecimal("300.00"), agreement2.getId())
        );

        return Stream.of(
                Arguments.of(agreement1, accountsForAgreement1),
                Arguments.of(agreement2, accountsForAgreement2),
                Arguments.of(new Agreement(), List.of())
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForTestGetAccounts")
    void testGetAccounts(Agreement agreement, List<Account> expectedAccounts) {
        when(accountDao.findByAgreementId(agreement.getId())).thenReturn(expectedAccounts);
        List<Account> actualAccounts = accountServiceImpl.getAccounts(agreement);
        Assertions.assertEquals(expectedAccounts, actualAccounts);
    }

    public static Stream<Arguments> provideParametersForMakeTransferMethod() {
        return Stream.of(
                Arguments.of(1L, 2L, new BigDecimal("50.00"), true, new BigDecimal("50.00"), new BigDecimal("100.00")),
                Arguments.of(1L, 2L, new BigDecimal("150.00"), false, new BigDecimal("100.00"), new BigDecimal("50.00")),
                Arguments.of(1L, 2L, BigDecimal.ZERO, false, new BigDecimal("100.00"), new BigDecimal("50.00")),
                Arguments.of(1L, 2L, new BigDecimal("-10.00"), false, new BigDecimal("100.00"), new BigDecimal("50.00")),
                Arguments.of(999L, 2L, new BigDecimal("50.00"), false, null, null),
                Arguments.of(1L, 999L, new BigDecimal("50.00"), false, new BigDecimal("100.00"), null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideParametersForMakeTransferMethod")
    void testMakeTransfer(Long sourceAccountId, Long destinationAccountId, BigDecimal transferSum, boolean expectedResult, BigDecimal expectedSourceAmount, BigDecimal expectedDestinationAmount) {
        Account sourceAccount = createAccount(1L, new BigDecimal("100.00"));
        Account destinationAccount = createAccount(2L, new BigDecimal("50.00"));

        lenient().when(accountDao.findById(sourceAccountId)).thenReturn(sourceAccountId == 999L ? Optional.empty() : Optional.of(sourceAccount));
        lenient().when(accountDao.findById(destinationAccountId)).thenReturn(destinationAccountId == 999L ? Optional.empty() : Optional.of(destinationAccount));

        if (sourceAccountId == 999L) {
            Assertions.assertThrows(AccountException.class, () -> {
                accountServiceImpl.makeTransfer(sourceAccountId, destinationAccountId, transferSum);
            });
            return;
        }

        if (destinationAccountId == 999L) {
            Assertions.assertThrows(AccountException.class, () -> {
                accountServiceImpl.makeTransfer(sourceAccountId, destinationAccountId, transferSum);
            });
            return;
        }

        boolean actualResult = accountServiceImpl.makeTransfer(sourceAccountId, destinationAccountId, transferSum);
        Assertions.assertEquals(expectedResult, actualResult);

        if (expectedResult) {
            verify(accountDao).save(sourceAccount);
            verify(accountDao).save(destinationAccount);
            Assertions.assertEquals(expectedSourceAmount, sourceAccount.getAmount());
            Assertions.assertEquals(expectedDestinationAmount, destinationAccount.getAmount());
        } else {
            verify(accountDao, never()).save(any());
        }
    }

    private Account createAccount(Long accountId, BigDecimal initialAmount) {
        Account account = new Account();
        account.setId(accountId);
        account.setAmount(initialAmount);
        account.setType(0);
        account.setNumber("12345");
        account.setAgreementId(1L);
        return account;
    }


    private static Account createAccount(Long accountId, BigDecimal amount, Long agreementId) {
        Account account = new Account();
        account.setId(accountId);
        account.setAmount(amount);
        account.setAgreementId(agreementId);
        account.setType(0);  // Пример значения
        account.setNumber("12345");  // Пример значения
        return account;
    }
}