import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.gihan.model.Account;
import com.gihan.model.Expense;
import com.gihan.model.Frequency;

public class ForecasterTest_Expenses {

    @Test
    public void shouldForecastSameBalance_whenThereAreNoTransactionsToApply() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now(), Collections.emptyList());

        assertThat(forecastedBalance, is(account.getBalance()));
    }

    @Test
    public void shouldForecastSameBalance_whenExpenseIs_inThePast() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense pastExpense = new Expense(new BigDecimal(25L), "phone bill", Frequency.MONTHLY, LocalDate.now().minusDays(1));
        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now(), Collections.singletonList(pastExpense));

        assertThat(forecastedBalance, is(account.getBalance()));
    }

    @Test
    public void shouldForecastSameBalance_whenExpenseIs_onSearchDate() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense travel = new Expense(new BigDecimal(40L), "travel", Frequency.WEEKLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusWeeks(1), Collections.singletonList(travel));

        assertThat(forecastedBalance, is(new BigDecimal(100L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_singleExpense() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense phoneBill = new Expense(new BigDecimal(25L), "phone bill", Frequency.MONTHLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(1), Collections.singletonList(phoneBill));

        assertThat(forecastedBalance, is(new BigDecimal(75L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_multipleExpenses() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense phoneBill = new Expense(new BigDecimal(25L), "phone bill", Frequency.MONTHLY, LocalDate.now().plusWeeks(1));
        Expense travel = new Expense(new BigDecimal(5L), "train fare", Frequency.WEEKLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(1), Arrays.asList(phoneBill, travel));

        assertThat(forecastedBalance, is(new BigDecimal(55L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_multipleOccurrencesOfSameExpense() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense phoneBill = new Expense(new BigDecimal(25L), "phone bill", Frequency.MONTHLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(2), Collections.singletonList(phoneBill));

        assertThat(forecastedBalance, is(new BigDecimal(50L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_twoOneOffExpenses() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense travel = new Expense(new BigDecimal(40L), "travel", Frequency.ONCE_OFF, LocalDate.now().plusDays(1));
        Expense someBill = new Expense(new BigDecimal(40L), "travel", Frequency.ONCE_OFF, LocalDate.now().plusDays(2));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusWeeks(1), Arrays.asList(travel, someBill));

        assertThat(forecastedBalance, is(new BigDecimal(20L)));
    }
}