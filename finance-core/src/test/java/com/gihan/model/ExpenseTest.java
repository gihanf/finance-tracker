package com.gihan.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("An Expense")
class ExpenseTest {

    @Test
    @DisplayName("must have a name")
    public void someTest() throws Exception {
        Expense e = new Expense(new BigDecimal("1"), "asd");
        assertThat(e.getName(), is("asdd"));
    }

}