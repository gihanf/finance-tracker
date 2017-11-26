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

public class Expense {

    private BigDecimal amount;
    private String name;
    private Frequency frequency;
    private LocalDate firstPaymentDate;
    private static final TemporalAdjuster WEEKLY_ADJUSTER = TemporalAdjusters.ofDateAdjuster(d -> d.plusWeeks(1));
    private static final TemporalAdjuster MONTHLY_ADJUSTER = TemporalAdjusters.ofDateAdjuster(d -> d.plusMonths(1));

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
        return getNextPaymentDate(now());
    }

    public Optional<LocalDate> getNextPaymentDate(LocalDate date) {
        switch (frequency) {
            case ONCE_OFF: {
                if (firstPaymentDate.isAfter(date)) {
                    return Optional.of(firstPaymentDate);
                }
                return Optional.empty();
            }
            case WEEKLY: {
                return getNextRecurringDate(WEEKLY_ADJUSTER, date);
            }
            case MONTHLY: {
                return getNextRecurringDate(MONTHLY_ADJUSTER, date);
            }
        }

        return Optional.empty();
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

    public Stream<LocalDate> getNextPaymentDates() {
        Optional<LocalDate> nextPaymentDate = getNextPaymentDate();

        if (nextPaymentDate.isPresent()) {
            LocalDate nextDate = nextPaymentDate.get();
            return Stream.iterate(nextDate, d -> this.getNextPaymentDate(d).get());
        }
        return Stream.empty();
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
