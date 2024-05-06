package kz.test.limitservice.transactionlimitservice.transaction.service;

import kz.test.limitservice.transactionlimitservice.fx.service.FxService;
import kz.test.limitservice.transactionlimitservice.limit.service.LimitService;
import kz.test.limitservice.transactionlimitservice.transaction.model.entity.Transaction;
import kz.test.limitservice.transactionlimitservice.transaction.repository.TransactionRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

class TransactionServiceTest {
  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private LimitService limitService;

  @Mock
  private FxService fxService;

  private TransactionService transactionService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
    transactionService = new TransactionService(transactionRepository, limitService, fxService);
  }

  @Test
  void saveTransaction() {
    Transaction transaction = new Transaction();
    transaction.setCurrencyShortName("EUR");
    transaction.setSum(BigDecimal.valueOf(100));
    ZonedDateTime dateTime = ZonedDateTime.parse("2024-04-24T12:00:00Z", DateTimeFormatter.ISO_DATE_TIME);
    transaction.setDatetime(dateTime);

    when(fxService.getRateAtDate(any(String.class), any(LocalDate.class))).thenReturn(BigDecimal.valueOf(1.12));
    when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

    Transaction savedTransaction = transactionService.saveTransaction(transaction);

    assertEquals(BigDecimal.valueOf(89.29).setScale(2), savedTransaction.getUsdEquivalent().setScale(2));
  }

  @Test
  void getAllTransactions() {
    when(transactionRepository.findAll()).thenReturn(Collections.emptyList());

    List<Transaction> transactions = transactionService.getAllTransactions();

    verify(transactionRepository, times(1)).findAll();

    assertTrue(transactions.isEmpty());
  }

  @Test
  void getTransaction() {
    Transaction transaction = new Transaction();

    when(transactionRepository.findById(any(Long.class))).thenReturn(Optional.of(transaction));

    Transaction foundTransaction = transactionService.getTransaction(1L);

    verify(transactionRepository, times(1)).findById(1L);

    assertEquals(transaction, foundTransaction);
  }

  @Test
  void getTransactionsExceededLimit() {
    List<Transaction> transactions = Arrays.asList(new Transaction(),  new Transaction());

    when(transactionRepository.findByLimitExceeded(true)).thenReturn(transactions);

    List<Transaction> foundTransactions = transactionService.getTransactionsExceededLimit();

    verify(transactionRepository, times(1)).findByLimitExceeded(true);

    assertEquals(transactions, foundTransactions);
  }
}