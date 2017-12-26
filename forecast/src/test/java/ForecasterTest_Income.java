import com.gihan.model.Account;
import com.gihan.model.Frequency;
import com.gihan.model.Income;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ForecasterTest_Income {

    @Test
    public void shouldForecastSameBalance_whenThereAreNoTransactionsToApply() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now(), Collections.emptyList());

        assertThat(forecastedBalance, is(account.getBalance()));
    }

    @Test
    public void shouldForecastSameBalance_whenIncomeIs_inThePast() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Income pastIncome = new Income(new BigDecimal(25L), "rent", Frequency.MONTHLY, LocalDate.now().minusDays(1));
        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now(), Collections.singletonList(pastIncome));

        assertThat(forecastedBalance, is(account.getBalance()));
    }

    @Test
    public void shouldSameForecastBalance_whenIncomeIs_onSearchDate() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Income travel = new Income(new BigDecimal(40L), "travel", Frequency.WEEKLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusWeeks(1), Collections.singletonList(travel));

        assertThat(forecastedBalance, is(new BigDecimal(100L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_singleIncome() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Income phoneBill = new Income(new BigDecimal(25L), "rent", Frequency.MONTHLY, LocalDate.now().plusWeeks(2));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(1), Collections.singletonList(phoneBill));

        assertThat(forecastedBalance, is(new BigDecimal(125L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_multipleIncomes() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Income phoneBill = new Income(new BigDecimal(25L), "rent", Frequency.MONTHLY, LocalDate.now().plusWeeks(1));
        Income travel = new Income(new BigDecimal(5L), "train fare", Frequency.WEEKLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(1), Arrays.asList(phoneBill, travel));

        assertThat(forecastedBalance, is(new BigDecimal(145L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_multipleOccurrencesOfSameIncome() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Income phoneBill = new Income(new BigDecimal(25L), "rent", Frequency.MONTHLY, LocalDate.now().plusWeeks(1));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(2), Collections.singletonList(phoneBill));

        assertThat(forecastedBalance, is(new BigDecimal(150L)));
    }

    @Test
    public void shouldForecastBalance_affectedBy_twoOneOffIncomes() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Income travel = new Income(new BigDecimal(40L), "lotto win", Frequency.ONCE_OFF, LocalDate.now().plusDays(1));
        Income someBill = new Income(new BigDecimal(40L), "scratchie win", Frequency.ONCE_OFF, LocalDate.now().plusDays(2));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusWeeks(1), Arrays.asList(travel, someBill));

        assertThat(forecastedBalance, is(new BigDecimal(180L)));
    }
}
