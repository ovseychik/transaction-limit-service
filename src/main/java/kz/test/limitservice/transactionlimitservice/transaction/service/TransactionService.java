package kz.test.limitservice.transactionlimitservice.transaction.service;

import kz.test.limitservice.transactionlimitservice.transaction.model.entity.Transaction;
import kz.test.limitservice.transactionlimitservice.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

  private final TransactionRepository transactionRepository;

  @Autowired
  public TransactionService(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  public Transaction saveTransaction(Transaction transaction) {
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
}
