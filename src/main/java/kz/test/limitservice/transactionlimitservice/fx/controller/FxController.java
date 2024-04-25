package kz.test.limitservice.transactionlimitservice.fx.controller;

import kz.test.limitservice.transactionlimitservice.fx.model.entity.FxRate;
import kz.test.limitservice.transactionlimitservice.fx.service.FxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/forex")
public class FxController {

  private FxService fxService;

  @Autowired
  public FxController(FxService fxService) {
    this.fxService = fxService;
  }

  @PostMapping
  public List<FxRate> updateRates(@RequestParam LocalDate from, @RequestParam LocalDate to) {
    return fxService.updateRates(from, to);
  }

  @GetMapping
  public List<FxRate> getFx() {
    return fxService.getAllRates();
  }
}
