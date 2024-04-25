package kz.test.limitservice.transactionlimitservice.fx.repository;

import kz.test.limitservice.transactionlimitservice.fx.model.entity.FxRate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FxRepository extends JpaRepository<FxRate, Long> {
}
