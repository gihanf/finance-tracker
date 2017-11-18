package com.gihan.model;

import java.util.Currency;

public class Expense {

    private Currency amount;
    private String name;

    public Expense(Currency amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public Currency getAmount() {
        return amount;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return super.toString();
    }

//    @Override
//    public boolean equals(Object other) {
//        return EqualsBuilder.reflectionEquals(this, other);
//    }
//
//    @Override
//    public int hashCode() {
//        return HashCodeBuilder.reflectionHashCode(this);
//    }
}
