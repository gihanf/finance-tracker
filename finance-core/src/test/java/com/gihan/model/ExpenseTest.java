package com.gihan.model;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;

import org.junit.Test;

public class ExpenseTest {

    @Test
    public void testTest() throws Exception {
        Expense e = new Expense(new BigDecimal("1"), "asd");
        assertThat(e.getName(), is("asdd"));
    }

}