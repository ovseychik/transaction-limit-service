package kz.test.limitservice.transactionlimitservice.transaction.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import kz.test.limitservice.transactionlimitservice.transaction.model.ExpenseCategory;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
public class Transaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 10, nullable = false)
  private Long accountFrom;

  @Column(length = 10, nullable = false)
  private Long accountTo;

  @Column(length = 3, nullable = false)
  private String currencyShortName;

  @Column(precision = 8, scale = 2, nullable = false)
  private BigDecimal sum;

  @Enumerated(EnumType.STRING)
  private ExpenseCategory expenseCategory;

  @Column(nullable = false)
  private ZonedDateTime dateTime;

  private boolean limitExceeded;

  @Column(precision = 8, scale = 2, nullable = false)
  private BigDecimal usdEquivalent;

  @Transient
  private BigDecimal limitSum;

  @Transient
  private ZonedDateTime limitDateTime;

  @Transient
  private String limitCurrencyShortName;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getAccountFrom() {
    return accountFrom;
  }

  public void setAccountFrom(Long accountFrom) {
    this.accountFrom = accountFrom;
  }

  public Long getAccountTo() {
    return accountTo;
  }

  public void setAccountTo(Long accountTo) {
    this.accountTo = accountTo;
  }

  public String getCurrencyShortName() {
    return currencyShortName;
  }

  public void setCurrencyShortName(String currencyShortName) {
    this.currencyShortName = currencyShortName;
  }

  public BigDecimal getSum() {
    return sum;
  }

  public void setSum(BigDecimal sum) {
    this.sum = sum;
  }

  public ExpenseCategory getExpenseCategory() {
    return expenseCategory;
  }

  public void setExpenseCategory(ExpenseCategory expenseCategory) {
    this.expenseCategory = expenseCategory;
  }

  public ZonedDateTime getDateTime() {
    return dateTime;
  }

  public void setDatetime(ZonedDateTime dateTime) {
    this.dateTime = dateTime;
  }

  public BigDecimal getUsdEquivalent() {
    return usdEquivalent;
  }

  public void setUsdEquivalent(BigDecimal usdEquivalent) {
    this.usdEquivalent = usdEquivalent;
  }

  public BigDecimal getLimitSum() {
    return limitSum;
  }

  public void setLimitSum(BigDecimal limitSum) {
    this.limitSum = limitSum;
  }

  public ZonedDateTime getLimitDateTime() {
    return limitDateTime;
  }

  public void setLimitDateTime(ZonedDateTime limitDateTime) {
    this.limitDateTime = limitDateTime;
  }

  public String getLimitCurrencyShortName() {
    return limitCurrencyShortName;
  }

  public void setLimitCurrencyShortName(String limitCurrencyShortName) {
    this.limitCurrencyShortName = limitCurrencyShortName;
  }

  public boolean isLimitExceeded() {
    return limitExceeded;
  }

  public void setLimitExceeded(boolean limitExceeded) {
    this.limitExceeded = limitExceeded;
  }
}
