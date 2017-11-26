package com.gihan.model;

import static java.time.LocalDate.now;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses(value = {
        ExpenseTest.OnceOffExpenses.class,
        ExpenseTest.WeeklyExpenses.class,
        ExpenseTest.MonthlyExpenses.class
})
public class ExpenseTest {

    public static class OnceOffExpenses {

        @Test
        public void shouldReturnEmptyOptional_whenFetchingNextPaymentDate_forExpense_inThePast() throws Exception {
            Expense expenseInThePast = new Expense(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, LocalDate.of(1980, 5, 7));

            assertThat(expenseInThePast.getNextPaymentDate(), is(Optional.empty()));
        }

        @Test
        public void shouldReturnEmptyOptional_whenFetchingNextPaymentDate_forExpense_onCurrentDate() throws Exception {
            Expense expenseOnCurrentDate = new Expense(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, now());

            assertThat(expenseOnCurrentDate.getNextPaymentDate(), is(Optional.empty()));
        }

        @Test
        public void shouldReturnFirstPaymentDate_whenFetchingNextPaymentDate_forExpense_inTheFuture() throws Exception {
            LocalDate firstPaymentDate = LocalDate.of(3020, 5, 7);
            Expense futureExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, firstPaymentDate);

            assertThat(futureExpense.getNextPaymentDate(), is(Optional.of(firstPaymentDate)));
        }

        @Test
        public void shouldReturnEmptyStream_whenFetchingNextPaymentDate_andThereAreNoFuturePayments() throws Exception {
            LocalDate pastDate = now().minusDays(1);
            Expense futureExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, pastDate);

            Stream<LocalDate> nextPaymentDates = futureExpense.getNextPaymentDates();
            assertThat(nextPaymentDates.count(), is(0L));
        }
    }

    public static class MonthlyExpenses {

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forExpense_onCurrentDate() throws Exception {
            LocalDate currentDate = now();
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, currentDate);

            assertThat(recurringExpense.getNextPaymentDate(), is(Optional.of(currentDate.plusMonths(1))));
        }

        @Test
        public void shouldReturnFirstPaymentDate_whenFetchingNextPaymentDate_forExpense_inTheFuture() throws Exception {
            LocalDate firstPaymentDate = LocalDate.of(3020, 5, 7);
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, firstPaymentDate);

            assertThat(recurringExpense.getNextPaymentDate(), is(Optional.of(firstPaymentDate)));
        }

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forRecurringExpense_andIsAfter_firstPaymentDate() throws Exception {
            LocalDate pastDate = now().minusMonths(4);
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, pastDate);

            assertThat(recurringExpense.getNextPaymentDate(), is(Optional.of(pastDate.plusMonths(5))));
        }

        @Test
        public void shouldReturnNextPayments() throws Exception {
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, now().minusMonths(2));

            List<LocalDate> expectedPaymentDates = Arrays.asList(
                    now().plusMonths(1),
                    now().plusMonths(2),
                    now().plusMonths(3),
                    now().plusMonths(4));

            List<LocalDate> nextFourMonths = recurringExpense.getNextPaymentDates().limit(4).collect(Collectors.toList());
            assertThat(nextFourMonths, is(expectedPaymentDates));
        }
    }

    public static class WeeklyExpenses {

        @Test
        public void shouldReturnFirstPaymentDate_whenFetchingNextPaymentDate_forExpense_inTheFuture() throws Exception {
            LocalDate futureDate = LocalDate.of(3020, 5, 7);
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, futureDate);

            assertThat(recurringExpense.getNextPaymentDate(), is(Optional.of(futureDate)));
        }

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forExpense_onCurrentDate() throws Exception {
            LocalDate currentDate = now();
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, currentDate);

            assertThat(recurringExpense.getNextPaymentDate(), is(Optional.of(currentDate.plusWeeks(1))));
        }

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forExpenseAfter_firstPaymentDate() throws Exception {
            LocalDate pastDate = now().minusWeeks(4);
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, pastDate);

            assertThat(recurringExpense.getNextPaymentDate(), is(Optional.of(pastDate.plusWeeks(5))));
        }

        @Test
        public void shouldReturnNextPayments() throws Exception {
            Expense recurringExpense = new Expense(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, now().minusWeeks(2));

            List<LocalDate> expectedPaymentDates = Arrays.asList(
                    now().plusWeeks(1),
                    now().plusWeeks(2),
                    now().plusWeeks(3),
                    now().plusWeeks(4));

            List<LocalDate> nextFourMonths = recurringExpense.getNextPaymentDates().limit(4).collect(Collectors.toList());
            assertThat(nextFourMonths, is(expectedPaymentDates));
        }

    }
}