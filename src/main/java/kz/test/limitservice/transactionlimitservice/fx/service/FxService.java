package kz.test.limitservice.transactionlimitservice.fx.service;

import kz.test.limitservice.transactionlimitservice.fx.model.entity.FxRate;
import kz.test.limitservice.transactionlimitservice.fx.model.response.FxResponse;
import kz.test.limitservice.transactionlimitservice.fx.repository.FxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FxService {

  private FxRepository fxRepository;

  private final String apiKey;
  private final String baseUrl = "https://api.currencyapi.com/v3/historical";
  private final RestTemplate restTemplate;

  @Autowired
  public FxService(FxRepository fxRepository, @Value("${api_key") String apiKey) {
    this.fxRepository = fxRepository;
    this.restTemplate = new RestTemplate();
    this.apiKey = apiKey;
  }

  public List<FxRate> getAllRates() {
    return fxRepository.findAll();
  }

  /**
   * Maps response from external API to FxRate entities and save them in DB
   */
  public void updateRates(LocalDate from, LocalDate to) {
    List<FxRate> rates = new ArrayList<>();
    for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
      String url = createUrl(date);
      FxResponse response = restTemplate.getForObject(url, FxResponse.class);
      rates.addAll(convertToEntities(date, response));
    }
    fxRepository.saveAll(rates);
  }

  /**
   * Converts each currency data from response to FxRate
   */
  private List<FxRate> convertToEntities(LocalDate date, FxResponse response) {
    return response.getData().entrySet().stream()
        .map(entry -> {
          FxRate fxRate = new FxRate();
          fxRate.setCurrencyCode(entry.getKey());
          fxRate.setValue(entry.getValue().getValue());
          fxRate.setDate(date);
          return fxRate;
        })
        .collect(Collectors.toList());
  }

  /**
   * Constructs URL for given date
   */
  private String createUrl(LocalDate date) {
    String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    return baseUrl + "?apikey=" + apiKey + "&currencies=KZT%2CUSD%2CEUR%2CRUB&date=" + formattedDate;
  }
}
