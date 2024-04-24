package kz.test.limitservice.transactionlimitservice.transaction.repository;

import kz.test.limitservice.transactionlimitservice.transaction.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findByLimitExceeded(boolean limitExceeded);
}
