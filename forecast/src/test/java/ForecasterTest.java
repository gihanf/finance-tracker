import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import org.junit.Test;

import com.gihan.model.Account;

public class ForecasterTest {

    @Test
    public void shouldReturnSameBalance_whenThereAreNoTransactionsToApply() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now(), Collections.emptyList());

        assertThat(forecastedBalance, is(account.getBalance()));
    }
    
}