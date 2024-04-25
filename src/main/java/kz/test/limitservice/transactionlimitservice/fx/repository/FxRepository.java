package kz.test.limitservice.transactionlimitservice.fx.repository;

import kz.test.limitservice.transactionlimitservice.fx.model.entity.FxRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FxRepository extends JpaRepository<FxRate, Long> {
  Optional<FxRate> findByCurrencyCodeAndDate(String currencyCode, LocalDate date);
}
