package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.entity.enumeration.Currency;

import java.math.BigDecimal;

public interface ExchangeRateService {

    BigDecimal getExchangeRate(Currency from, Currency to);

}
