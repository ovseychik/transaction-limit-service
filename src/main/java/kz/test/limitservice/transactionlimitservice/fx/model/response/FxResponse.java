package kz.test.limitservice.transactionlimitservice.fx.model.response;

import java.util.Map;

public class FxResponse {

  private Meta meta;
  private Map<String, CurrencyData> data;

  public Meta getMeta() {
    return meta;
  }

  public void setMeta(Meta meta) {
    this.meta = meta;
  }

  public Map<String, CurrencyData> getData() {
    return data;
  }

  public void setData(Map<String, CurrencyData> data) {
    this.data = data;
  }
}
