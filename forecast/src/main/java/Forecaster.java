import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.gihan.model.Account;
import com.gihan.model.Transaction;
import com.gihan.model.TransactionType;

public class Forecaster {

    public static BigDecimal forecastBalanceForAccount(Account account, LocalDate forecastDate, List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return account.getBalance();
        }
        BigDecimal modifiedBalance = account.getBalance();
        for (Transaction transaction : transactions) {
            int txnsBeforeForecast = transaction.numberOfTransactionsBeforeDate(forecastDate);
            BigDecimal adjustmentAmount = transaction.getAmount().multiply(new BigDecimal(txnsBeforeForecast));
            modifiedBalance = transaction.getTransactionType().equals(TransactionType.DEBIT)
                    ? modifiedBalance.subtract(adjustmentAmount) : modifiedBalance.add(adjustmentAmount);
        }

        return modifiedBalance;
    }
}
