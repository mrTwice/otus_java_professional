package ru.otus.java.professional.yampolskiy.bank.service;

import ru.otus.java.professional.yampolskiy.bank.entity.Agreement;

import java.math.BigDecimal;


public interface PaymentProcessor {

    boolean makeTransfer(Agreement source, Agreement destination, int sourceType, int destinationType, BigDecimal amount);

    boolean makeTransferWithComission(Agreement source, Agreement destination,
                                      int sourceType, int destinationType,
                                      BigDecimal amount,
                                      BigDecimal comissionPercent);
}

