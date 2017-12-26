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
            BigDecimal adjustmentAmount = calculateAdjustmentAmount(transaction, txnsBeforeForecast);
            modifiedBalance = applyAdjustment(modifiedBalance, adjustmentAmount, transaction.getTransactionType());
        }

        return modifiedBalance;
    }

    private static BigDecimal applyAdjustment(BigDecimal modifiedBalance, BigDecimal adjustmentAmount, TransactionType transactionType) {
        return transactionType.equals(TransactionType.DEBIT) ? modifiedBalance.subtract(adjustmentAmount) : modifiedBalance.add(adjustmentAmount);
    }

    private static BigDecimal calculateAdjustmentAmount(Transaction transaction, int txnsBeforeForecast) {
        return transaction.getAmount().multiply(new BigDecimal(txnsBeforeForecast));
    }
}
