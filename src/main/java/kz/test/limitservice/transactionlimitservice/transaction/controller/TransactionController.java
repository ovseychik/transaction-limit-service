package kz.test.limitservice.transactionlimitservice.transaction.controller;

import kz.test.limitservice.transactionlimitservice.transaction.model.entity.Transaction;
import kz.test.limitservice.transactionlimitservice.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

  private final TransactionService transactionService;

  @Autowired
  public TransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  public ResponseEntity<Void> createTransaction(@RequestBody Transaction transaction) {
    transactionService.saveTransaction(transaction);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  public List<Transaction> getAllTransactions() {
    return transactionService.getAllTransactions();
  }

  @GetMapping("/{id}")
  public Transaction getTransaction(@PathVariable Long id) {
    return transactionService.getTransaction(id);
  }

  @GetMapping("/exceeded")
  public List<Transaction> getTransactionsExceededLimit() {
    return transactionService.getTransactionsExceededLimit();
  }
}
