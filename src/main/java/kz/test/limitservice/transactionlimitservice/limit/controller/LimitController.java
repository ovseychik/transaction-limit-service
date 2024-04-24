package kz.test.limitservice.transactionlimitservice.limit.controller;

import kz.test.limitservice.transactionlimitservice.limit.model.entity.Limit;
import kz.test.limitservice.transactionlimitservice.limit.service.LimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/limits")
public class LimitController {
  private final LimitService limitService;

  @Autowired
  public LimitController(LimitService limitService) {
    this.limitService = limitService;
  }

  @PostMapping
  public Limit createLimit(@RequestBody Limit limit) {
    return limitService.saveLimit(limit);
  }

  @GetMapping
  public List<Limit> getAllLimits() {
    return limitService.getAllLimits();
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }
}
