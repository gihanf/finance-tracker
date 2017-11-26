package com.gihan.model;

import static java.time.LocalDate.now;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Expense {

    private BigDecimal amount;
    private String name;
    private Frequency frequency;
    private LocalDate firstPaymentDate;

    public Expense(BigDecimal amount, String name, Frequency frequency, LocalDate firstPaymentDate) {
        this.amount = amount;
        this.name = name;
        this.frequency = frequency;
        this.firstPaymentDate = firstPaymentDate;
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

    public Optional<LocalDate> getNextPaymentDate() {
        switch (frequency) {
            case ONCE_OFF: {
                if (firstPaymentDate.isAfter(now())) {
                    return Optional.of(firstPaymentDate);
                }
                return Optional.empty();
            }
            case MONTHLY: {
                if (!firstPaymentDate.isAfter(now())) {
                    LocalDate nextPaymentDate = firstPaymentDate;
                    while (!nextPaymentDate.isAfter(now())) {
                        nextPaymentDate = nextPaymentDate.plusMonths(1);
                    }
                    return Optional.of(nextPaymentDate);
                }
                return Optional.of(firstPaymentDate);
            }
        }

        return Optional.empty();
    }

    public Stream<LocalDate> getNextPaymentDates() {
        Optional<LocalDate> nextPaymentDate = getNextPaymentDate();
        return nextPaymentDate
                .map(nextDate -> Stream.iterate(nextDate, d -> d.plusMonths(1)))
                .orElseGet(Stream::empty);
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
