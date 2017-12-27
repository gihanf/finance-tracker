package com.gihan.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income extends Transaction {

    public Income(BigDecimal amount, String name, Frequency frequency, LocalDate firstPaymentDate) {
        super(amount, name, frequency, TransactionType.CREDIT, firstPaymentDate);
    }
}
