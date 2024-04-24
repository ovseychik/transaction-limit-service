package kz.test.limitservice.transactionlimitservice.transaction.repository;

import kz.test.limitservice.transactionlimitservice.transaction.model.ExpenseCategory;
import kz.test.limitservice.transactionlimitservice.transaction.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findByLimitExceeded(boolean limitExceeded);

  @Query(
        """ 
         SELECT COALESCE(SUM(t.sum), 0) 
         FROM Transaction t 
         WHERE EXTRACT(YEAR FROM t.dateTime) = EXTRACT(YEAR FROM :dateTime) 
         AND EXTRACT(MONTH FROM t.dateTime) = EXTRACT(MONTH FROM :dateTime)  
         AND t.expenseCategory = :category
        """
  )
  Optional<BigDecimal> getSumOfTransactionsForMonthByCategory(
      ExpenseCategory category,
      ZonedDateTime dateTime
  );

}
