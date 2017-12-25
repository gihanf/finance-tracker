import com.gihan.model.Account;
import com.gihan.model.Expense;
import com.gihan.model.Frequency;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ForecasterTest_Credits {

    @Test
    public void shouldForecastBalance_affectedBy_singleCredit() throws Exception {
        Account account = new Account(new BigDecimal(100L), "savings");
        Expense phoneBill = new Expense(new BigDecimal(25L), "rent", Frequency.MONTHLY, LocalDate.now().plusWeeks(2));

        BigDecimal forecastedBalance = Forecaster.forecastBalanceForAccount(account, LocalDate.now().plusMonths(1), Collections.singletonList(phoneBill));

        assertThat(forecastedBalance, is(new BigDecimal(125L)));
    }


}
