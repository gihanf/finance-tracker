import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.gihan.model.Account;
import com.gihan.model.Transaction;

public class Forecaster {

    public static BigDecimal forecastBalanceForAccount(Account account, LocalDate localDate, List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return account.getBalance();
        }
        return BigDecimal.ONE;
    }
}
