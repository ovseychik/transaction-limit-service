package kz.test.limitservice.transactionlimitservice.transaction.service;

import kz.test.limitservice.transactionlimitservice.limit.service.LimitService;
import kz.test.limitservice.transactionlimitservice.transaction.model.entity.Transaction;
import kz.test.limitservice.transactionlimitservice.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;
  private final LimitService limitService;

  @Autowired
  public TransactionService(TransactionRepository transactionRepository, LimitService limitService) {
    this.transactionRepository = transactionRepository;
    this.limitService = limitService;
  }

  public Transaction saveTransaction(Transaction transaction) {
    checkAndSetLimitExceeded(transaction);
    return transactionRepository.save(transaction);
  }

  public List<Transaction> getAllTransactions() {
    return transactionRepository.findAll();
  }

  public Transaction getTransaction(Long id) {
    return transactionRepository.findById(id).orElse(null);
  }

  public List<Transaction> getTransactionsExceededLimit() {
    return transactionRepository.findByLimitExceeded(true);
  }

  private void checkAndSetLimitExceeded(Transaction transaction) {
    BigDecimal sumOfTransactions = transactionRepository.getSumOfTransactionsForMonthByCategory( // You would need to implement this method
        transaction.getExpenseCategory(),
        transaction.getDateTime()
    ).orElse(BigDecimal.ZERO);

    BigDecimal limit = limitService.getCurrentLimitByCategory( // You would need to implement this method
        transaction.getExpenseCategory(),
        transaction.getDateTime()
    ).orElse(new BigDecimal(1000)); // Default value

    if (sumOfTransactions.add(transaction.getSum()).compareTo(limit) > 0) {
      transaction.setLimitExceeded(true);
    }
  }
}
