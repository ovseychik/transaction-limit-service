package kz.test.limitservice.transactionlimitservice.transaction.service;

import kz.test.limitservice.transactionlimitservice.fx.service.FxService;
import kz.test.limitservice.transactionlimitservice.limit.model.entity.Limit;
import kz.test.limitservice.transactionlimitservice.limit.service.LimitService;
import kz.test.limitservice.transactionlimitservice.transaction.model.entity.Transaction;
import kz.test.limitservice.transactionlimitservice.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final LimitService limitService;
  private final FxService fxService;

  @Autowired
  public TransactionService(
      TransactionRepository transactionRepository,
      LimitService limitService,
      FxService fxService
  ) {
    this.transactionRepository = transactionRepository;
    this.limitService = limitService;
    this.fxService = fxService;
  }

  public Transaction saveTransaction(Transaction transaction) {
    BigDecimal usdEquivalent;

    if ("USD".equals(transaction.getCurrencyShortName())) {
      usdEquivalent = transaction.getSum();
    } else {
      BigDecimal fxRateForTransactionCurrency = fxService.getRateAtDate(
          transaction.getCurrencyShortName(),
          LocalDate.from(transaction.getDateTime())
      );

      usdEquivalent = transaction.getSum().divide(fxRateForTransactionCurrency, 2, RoundingMode.HALF_UP);
    }

    transaction.setUsdEquivalent(usdEquivalent);
    checkAndSetLimitExceeded(transaction);

    return transactionRepository.save(transaction);
  }

  public List<Transaction> getAllTransactions() {
    List<Transaction> transactions = transactionRepository.findAll();
    transactions.forEach(this::populateExceededLimitFields);

    return transactions;
  }

  public Transaction getTransaction(Long id) {
    Transaction transaction = transactionRepository.findById(id).orElse(null);
    if (transaction != null) {
      populateExceededLimitFields(transaction);
    }

    return transaction;
  }

  public List<Transaction> getTransactionsExceededLimit() {
    List<Transaction> transactions = transactionRepository.findByLimitExceeded(true);
    transactions.forEach(this::populateExceededLimitFields);
    return transactions;
  }

  private void checkAndSetLimitExceeded(Transaction transaction) {
    BigDecimal sumOfTransactions = transactionRepository.getSumOfTransactionsForMonthByCategory(
        transaction.getExpenseCategory(),
        transaction.getDateTime()
    ).orElse(BigDecimal.ZERO);

    sumOfTransactions = sumOfTransactions.add(transaction.getUsdEquivalent());

    Optional<Limit> optionalLimit = limitService.getCurrentLimitByCategory(
        transaction.getExpenseCategory(),
        transaction.getDateTime()
    );

    BigDecimal limit = optionalLimit.map(Limit::getSum)
        // Default value
        .orElse(new BigDecimal(1000));

    if (sumOfTransactions.compareTo(limit) > 0) {
      transaction.setLimitExceeded(true);
    }
  }

  private Transaction populateExceededLimitFields(Transaction transaction) {
    if (transaction.isLimitExceeded()) {
      transaction.setLimitSum(new BigDecimal(1000)); // Default limit sum
      transaction.setLimitDateTime(
          transaction
              .getDateTime()
              .withDayOfMonth(1)
              .toLocalDate()
              .atStartOfDay(
                  transaction.getDateTime().getZone()
              )
      );
      transaction.setLimitCurrencyShortName("USD"); // Default currency

      Optional<Limit> optionalLimit = limitService.getCurrentLimitByCategory(
          transaction.getExpenseCategory(),
          transaction.getDateTime()
      );

      if (optionalLimit.isPresent()) {
        Limit limit = optionalLimit.get();
        transaction.setLimitSum(limit.getSum());
        transaction.setLimitCurrencyShortName(limit.getCurrencyShortName());
        transaction.setLimitDateTime(limit.getDatetime());
      }
    }

    return transaction;
  }
}
