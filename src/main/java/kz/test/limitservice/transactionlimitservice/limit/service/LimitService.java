package kz.test.limitservice.transactionlimitservice.limit.service;

import kz.test.limitservice.transactionlimitservice.limit.model.entity.Limit;
import kz.test.limitservice.transactionlimitservice.limit.repository.LimitRepository;
import kz.test.limitservice.transactionlimitservice.transaction.model.ExpenseCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LimitService {

  private final LimitRepository limitRepository;

  @Autowired
  public LimitService(LimitRepository limitRepository) {
    this.limitRepository = limitRepository;
  }

  public Limit saveLimit(Limit limit) {
    if (limit.getId() != null) {
      if (limitRepository.existsById(limit.getId())) {
        throw new IllegalArgumentException(
            "Limit with id " + limit.getId() + " already exists. Cannot modify it."
        );
      }
    }

    return limitRepository.save(limit);
  }

  public List<Limit> getAllLimits() {
    return limitRepository.findAll();
  }

  public Optional<Limit> getCurrentLimitByCategory(ExpenseCategory category, ZonedDateTime dateTime) {
    return limitRepository.getCurrentLimitByCategory(category, dateTime);
  }
}
