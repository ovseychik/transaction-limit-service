package kz.test.limitservice.transactionlimitservice.limit.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import kz.test.limitservice.transactionlimitservice.transaction.model.ExpenseCategory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "limits")
public class Limit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(precision = 8, scale = 2)
  private BigDecimal sum;

  @Column(nullable = false)
  private ZonedDateTime datetime;

  @Column(length = 3, nullable = false)
  private String currencyShortName = "USD";

  @Enumerated(EnumType.STRING)
  private ExpenseCategory expenseCategory;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public void setSum(BigDecimal sum) {
    this.sum = sum;
  }

  public ZonedDateTime getDatetime() {
    return datetime;
  }

  public void setDatetime(ZonedDateTime datetime) {
    this.datetime = datetime;
  }

  public String getCurrencyShortName() {
    return currencyShortName;
  }

  public void setCurrencyShortName(String currencyShortName) {
    this.currencyShortName = currencyShortName;
  }

  public ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }

  public void setExpenseCategory(ExpenseCategory expenseCategory) {
    this.expenseCategory = expenseCategory;
  }
}
