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
                // TODO: Try and make this block just the same as the other frequencies
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

    public int numberOfTransactionsBeforeDate(LocalDate searchDate) {
        return isRecurring() ? getRecurringTransactionsBeforeDate(searchDate) : getOnceOffTransactionsBeforeDate(searchDate);
    }

    private int getOnceOffTransactionsBeforeDate(LocalDate searchDate) {
        Optional<LocalDate> nextDate = getNextPaymentDate();
        if (nextDate.isPresent()) {
            LocalDate date = nextDate.get();
            return date.isBefore(searchDate) ? 1 : 0;
        }
        return 0;
    }

    private int getRecurringTransactionsBeforeDate(LocalDate searchDate) {
        Optional<LocalDate> nextDate = getNextPaymentDate();
        int numTxns = 0;
        while (nextDate.isPresent()) {
            LocalDate date = nextDate.get();
            if (date.isAfter(searchDate) || date.isEqual(searchDate)) {
                break;
            } else {
                numTxns++;
            }
            nextDate = this.getNextPaymentDate(date);
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

    private boolean isRecurring() {
        return frequency != Frequency.ONCE_OFF;
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
