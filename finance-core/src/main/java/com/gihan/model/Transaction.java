package com.gihan.model;

import static java.time.LocalDate.now;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class Transaction {
    private static final TemporalAdjuster WEEKLY_ADJUSTER = TemporalAdjusters.ofDateAdjuster(d -> d.plusWeeks(1));
    private static final TemporalAdjuster MONTHLY_ADJUSTER = TemporalAdjusters.ofDateAdjuster(d -> d.plusMonths(1));

    private BigDecimal amount;
    private String name;
    private Frequency frequency;
    private LocalDate firstPaymentDate;
    private TransactionType transactionType;

    public Transaction(BigDecimal amount, String name, Frequency frequency, LocalDate firstPaymentDate, TransactionType transactionType) {
        this.amount = amount;
        this.name = name;
        this.frequency = frequency;
        this.firstPaymentDate = firstPaymentDate;
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public Optional<LocalDate> getNextPaymentDate() {
        return getNextPaymentDate(now());
    }

    public Optional<LocalDate> getNextPaymentDate(LocalDate fromDate) {
        switch (frequency) {
            case ONCE_OFF: {
                if (firstPaymentDate.isAfter(fromDate) || firstPaymentDate.isEqual(fromDate)) {
                    return Optional.of(firstPaymentDate);
                }
                return Optional.empty();
            }
            case WEEKLY: {
                return getNextRecurringDate(WEEKLY_ADJUSTER, fromDate);
            }
            case MONTHLY: {
                return getNextRecurringDate(MONTHLY_ADJUSTER, fromDate);
            }
        }

        return Optional.empty();
    }

    public Stream<LocalDate> getNextPaymentDates() {
        Optional<LocalDate> nextPaymentDate = getNextPaymentDate();

        if (nextPaymentDate.isPresent()) {
            LocalDate nextDate = nextPaymentDate.get();
            return Stream.iterate(nextDate, d -> this.getNextPaymentDate(d).get());
        }
        return Stream.empty();
    }

    public int numberOfTransactionsBeforeDate(LocalDate searchDateInclusive) {
        if (!getNextPaymentDate().isPresent()) {
            return 0;
        }
        int numTxns = 0;
        Optional<LocalDate> nextDate = getNextPaymentDate();
        while(nextDate.isPresent()) {
            LocalDate date = nextDate.get();
            if (!date.isAfter(searchDateInclusive)) {
                numTxns++;
            } else {
                break;
            }
            nextDate = this.getNextPaymentDate(date);
            if (nextDate.isPresent()) {
                if(nextDate.get().isEqual(date)) {
                    break;
                }
            }
        }
        return numTxns;
    }

    private Optional<LocalDate> getNextRecurringDate(TemporalAdjuster adjuster, LocalDate date) {
        if (!firstPaymentDate.isAfter(date)) {
            LocalDate nextPaymentDate = firstPaymentDate;
            while (!nextPaymentDate.isAfter(date)) {
                nextPaymentDate = (LocalDate) adjuster.adjustInto(nextPaymentDate);
            }
            return Optional.of(nextPaymentDate);
        }
        return Optional.of(firstPaymentDate);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
