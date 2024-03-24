package ru.hits.trb.trbcore.client.exchangerate;

import ru.hits.trb.trbcore.entity.enumeration.Currency;

import java.math.BigDecimal;

public interface ExchangeRateClient {

    BigDecimal getExchangeRate(Currency from, Currency to);

}
