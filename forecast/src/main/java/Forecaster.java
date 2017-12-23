import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.gihan.model.Account;
import com.gihan.model.Transaction;

public class Forecaster {

    public static BigDecimal forecastBalanceForAccount(Account account, LocalDate forecastDate, List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return account.getBalance();
        }
        BigDecimal modifiedBalance = account.getBalance();
        for (Transaction transaction : transactions) {
            int txnsBeforeForecast = transaction.numberOfTransactionsBeforeDate(forecastDate);
            BigDecimal adjustmentAmount = transaction.getAmount().multiply(new BigDecimal(txnsBeforeForecast));
            modifiedBalance = modifiedBalance.subtract(adjustmentAmount);
        }

        return modifiedBalance;
    }
}
