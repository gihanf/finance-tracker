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

public class ForecasterTest {

    @Test
    public void shouldForecastSameBalance_whenThereAreNoTransactionsToApply() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now(), Collections.emptyList());

        assertThat(forecastedBalance, is(account.getBalance()));
    }

    @Test
    public void shouldForecastBalance_affectedByExpenses() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense phoneBill = new Expense(new BigDecimal(25L), "phone bill", Frequency.MONTHLY, LocalDate.now().plusWeeks(1));
        
        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(1), Collections.singletonList(phoneBill));

        assertThat(forecastedBalance, is(new BigDecimal(75L)));
    }
    
    @Test
    public void shouldForecastBalance_affectedByMultipleOccurrencesOfSameExpense() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense phoneBill = new Expense(new BigDecimal(25L), "phone bill", Frequency.MONTHLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(2), Collections.singletonList(phoneBill));

        assertThat(forecastedBalance, is(new BigDecimal(50L)));
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
    public void shouldForecasetBalance_affectedBy_expenseLandingOnSearchDate() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense travel = new Expense(new BigDecimal(40L), "travel", Frequency.WEEKLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusWeeks(1), Collections.singletonList(travel));

        assertThat(forecastedBalance, is(new BigDecimal(60L)));
    }
    
}