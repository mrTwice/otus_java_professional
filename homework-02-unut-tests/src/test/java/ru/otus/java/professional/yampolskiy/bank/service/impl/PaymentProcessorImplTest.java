package ru.otus.java.professional.yampolskiy.bank.service.impl;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.java.professional.yampolskiy.bank.entity.Account;
import ru.otus.java.professional.yampolskiy.bank.entity.Agreement;
import ru.otus.java.professional.yampolskiy.bank.service.AccountService;
import ru.otus.java.professional.yampolskiy.bank.service.exception.AccountException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentProcessorImplTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private PaymentProcessorImpl paymentProcessor;

    @ParameterizedTest
    @MethodSource("provideTransferParams")
    void testMakeTransfer(Agreement source, Agreement destination, int sourceType,
                          int destinationType, BigDecimal amount, List<Account> sourceAccounts,
                          List<Account> destinationAccounts, boolean expectedResult) {

        when(accountService.getAccounts(source)).thenReturn(sourceAccounts);
        when(accountService.getAccounts(destination)).thenReturn(destinationAccounts);
        when(accountService.makeTransfer(anyLong(), anyLong(), eq(amount))).thenReturn(expectedResult);

        boolean result = paymentProcessor.makeTransfer(source, destination, sourceType, destinationType, amount);

        assertEquals(expectedResult, result);
        verify(accountService).getAccounts(source);
        verify(accountService).getAccounts(destination);
        verify(accountService).makeTransfer(anyLong(), anyLong(), eq(amount));
    }

    @ParameterizedTest
    @MethodSource("provideComissionTransferParams")
    void testMakeTransferWithComission(Agreement source, Agreement destination, int sourceType,
                                       int destinationType, BigDecimal amount, BigDecimal comissionPercent,
                                       List<Account> sourceAccounts, List<Account> destinationAccounts, boolean expectedResult) {

        when(accountService.getAccounts(source)).thenReturn(sourceAccounts);
        when(accountService.getAccounts(destination)).thenReturn(destinationAccounts);
        when(accountService.makeTransfer(anyLong(), anyLong(), eq(amount))).thenReturn(expectedResult);

        boolean result = paymentProcessor.makeTransferWithComission(source, destination, sourceType, destinationType, amount, comissionPercent);

        verify(accountService).charge(anyLong(), eq(amount.negate().multiply(comissionPercent)));
        verify(accountService).makeTransfer(anyLong(), anyLong(), eq(amount));
        assertEquals(expectedResult, result);
    }

    @ParameterizedTest
    @MethodSource("findAccountTestCases")
    void testFindAccount(Agreement agreement, int accountType, List<Account> accounts, Integer expectedAccountType) {
        when(accountService.getAccounts(agreement)).thenReturn(accounts);

        if (expectedAccountType != null) {
            Account foundAccount = paymentProcessor.findAccount(agreement, accountType);
            assertEquals(expectedAccountType, foundAccount.getType());
        } else {
            assertThrows(AccountException.class, () -> {
                paymentProcessor.findAccount(agreement, accountType);
            });
        }
    }


    private static Stream<Arguments> provideTransferParams() {
        Agreement source = new Agreement();
        Agreement destination = new Agreement();

        Account sourceAccount = createAccount(1L, BigDecimal.valueOf(100), 1L);
        Account destinationAccount = createAccount(2L, BigDecimal.valueOf(200), 2L);

        return Stream.of(
                Arguments.of(source, destination, 0, 0, BigDecimal.TEN, List.of(sourceAccount), List.of(destinationAccount), true),
                Arguments.of(source, destination, 0, 0, BigDecimal.TEN, List.of(sourceAccount), List.of(destinationAccount), false)
        );
    }

    private static Stream<Arguments> provideComissionTransferParams() {
        Agreement source = new Agreement();
        Agreement destination = new Agreement();

        Account sourceAccount = createAccount(1L, BigDecimal.valueOf(100), 1L);
        Account destinationAccount = createAccount(2L, BigDecimal.valueOf(200), 2L);

        return Stream.of(
                Arguments.of(source, destination, 0, 0, BigDecimal.TEN, BigDecimal.valueOf(0.05), List.of(sourceAccount), List.of(destinationAccount), true),
                Arguments.of(source, destination, 0, 0, BigDecimal.TEN, BigDecimal.valueOf(0.05), List.of(sourceAccount), List.of(destinationAccount), false)
        );
    }

    static Stream<Arguments> findAccountTestCases() {
        return Stream.of(
                Arguments.of(new Agreement(), 0, Arrays.asList(createAccount(1L, BigDecimal.TEN, 100L), createAccount(2L, BigDecimal.ZERO, 200L)), 0),
                Arguments.of(new Agreement(), 1, Arrays.asList(createAccount(1L, BigDecimal.TEN, 100L), createAccount(2L, BigDecimal.ZERO, 200L)), null),
                Arguments.of(new Agreement(), 0, Arrays.asList(createAccount(3L, BigDecimal.valueOf(100), 300L)), 0) // Счет найден
        );
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
