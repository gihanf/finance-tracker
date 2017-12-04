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
        TransactionTest.OnceOffTransactions.class,
        TransactionTest.WeeklyTransactions.class,
        TransactionTest.MonthlyTransactions.class
})
public class TransactionTest {

    public static class OnceOffTransactions {

        @Test
        public void shouldReturnEmptyOptional_whenFetchingNextPaymentDate_forTransaction_inThePast() throws Exception {
            Transaction transactionInThePast = new Transaction(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, LocalDate.of(1980, 5, 7));

            assertThat(transactionInThePast.getNextPaymentDate(), is(Optional.empty()));
        }

        @Test
        public void shouldReturnEmptyOptional_whenFetchingNextPaymentDate_forTransaction_onCurrentDate() throws Exception {
            Transaction transactionOnCurrentDate = new Transaction(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, now());

            assertThat(transactionOnCurrentDate.getNextPaymentDate(), is(Optional.empty()));
        }

        @Test
        public void shouldReturnFirstPaymentDate_whenFetchingNextPaymentDate_forTransaction_inTheFuture() throws Exception {
            LocalDate firstPaymentDate = LocalDate.of(3020, 5, 7);
            Transaction futureTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, firstPaymentDate);

            assertThat(futureTransaction.getNextPaymentDate(), is(Optional.of(firstPaymentDate)));
        }

        @Test
        public void shouldReturnEmptyStream_whenFetchingNextPaymentDate_andThereAreNoFuturePayments() throws Exception {
            LocalDate pastDate = now().minusDays(1);
            Transaction futureTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.ONCE_OFF, pastDate);

            Stream<LocalDate> nextPaymentDates = futureTransaction.getNextPaymentDates();
            assertThat(nextPaymentDates.count(), is(0L));
        }
    }

    public static class MonthlyTransactions {

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forTransaction_onCurrentDate() throws Exception {
            LocalDate currentDate = now();
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, currentDate);

            assertThat(recurringTransaction.getNextPaymentDate(), is(Optional.of(currentDate.plusMonths(1))));
        }

        @Test
        public void shouldReturnFirstPaymentDate_whenFetchingNextPaymentDate_forTransaction_inTheFuture() throws Exception {
            LocalDate firstPaymentDate = LocalDate.of(3020, 5, 7);
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, firstPaymentDate);

            assertThat(recurringTransaction.getNextPaymentDate(), is(Optional.of(firstPaymentDate)));
        }

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forRecurringTransaction_andIsAfter_firstPaymentDate() throws Exception {
            LocalDate pastDate = now().minusMonths(4);
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, pastDate);

            assertThat(recurringTransaction.getNextPaymentDate(), is(Optional.of(pastDate.plusMonths(5))));
        }

        @Test
        public void shouldReturnNextPayments() throws Exception {
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.MONTHLY, now().minusMonths(2));

            List<LocalDate> expectedPaymentDates = Arrays.asList(
                    now().plusMonths(1),
                    now().plusMonths(2),
                    now().plusMonths(3),
                    now().plusMonths(4));

            List<LocalDate> nextFourMonths = recurringTransaction.getNextPaymentDates().limit(4).collect(Collectors.toList());
            assertThat(nextFourMonths, is(expectedPaymentDates));
        }
    }

    public static class WeeklyTransactions {

        @Test
        public void shouldReturnFirstPaymentDate_whenFetchingNextPaymentDate_forTransaction_inTheFuture() throws Exception {
            LocalDate futureDate = LocalDate.of(3020, 5, 7);
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, futureDate);

            assertThat(recurringTransaction.getNextPaymentDate(), is(Optional.of(futureDate)));
        }

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forTransaction_onCurrentDate() throws Exception {
            LocalDate currentDate = now();
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, currentDate);

            assertThat(recurringTransaction.getNextPaymentDate(), is(Optional.of(currentDate.plusWeeks(1))));
        }

        @Test
        public void shouldReturnNextPaymentDate_whenFetchingNextPaymentDate_forTransactionAfter_firstPaymentDate() throws Exception {
            LocalDate pastDate = now().minusWeeks(4);
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, pastDate);

            assertThat(recurringTransaction.getNextPaymentDate(), is(Optional.of(pastDate.plusWeeks(5))));
        }

        @Test
        public void shouldReturnNextPayments() throws Exception {
            Transaction recurringTransaction = new Transaction(new BigDecimal("10"), "hair cut", Frequency.WEEKLY, now().minusWeeks(2));

            List<LocalDate> expectedPaymentDates = Arrays.asList(
                    now().plusWeeks(1),
                    now().plusWeeks(2),
                    now().plusWeeks(3),
                    now().plusWeeks(4));

            List<LocalDate> nextFourMonths = recurringTransaction.getNextPaymentDates().limit(4).collect(Collectors.toList());
            assertThat(nextFourMonths, is(expectedPaymentDates));
        }

    }
}