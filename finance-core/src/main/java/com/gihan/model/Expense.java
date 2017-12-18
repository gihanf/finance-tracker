package com.gihan.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense extends Transaction {

    public Expense(BigDecimal amount, String name, Frequency frequency, LocalDate firstPaymentDate) {
        super(amount, name, frequency, firstPaymentDate, TransactionType.DEBIT);
    }
}
