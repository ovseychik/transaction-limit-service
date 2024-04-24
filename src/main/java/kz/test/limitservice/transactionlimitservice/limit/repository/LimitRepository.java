package kz.test.limitservice.transactionlimitservice.limit.repository;

import kz.test.limitservice.transactionlimitservice.limit.model.entity.Limit;
import kz.test.limitservice.transactionlimitservice.transaction.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Long> {
  @Query(
      """
        SELECT l.sum
        FROM Limit l
        WHERE l.expenseCategory = :category
        AND l.datetime <= :dateTime
        AND EXTRACT(YEAR FROM l.datetime) = EXTRACT(YEAR FROM :dateTime)
        AND EXTRACT(MONTH FROM l.datetime) = EXTRACT(MONTH FROM :dateTime)
        ORDER BY l.datetime DESC
      """
  )
  Optional<BigDecimal> getCurrentLimitByCategory(
      ExpenseCategory category,
      ZonedDateTime dateTime);

}
