package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.client.exchangerate.ExchangeRateClient;
import ru.hits.trb.trbcore.entity.enumeration.Currency;
import ru.hits.trb.trbcore.service.ExchangeRateService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private final ExchangeRateClient exchangeRateClient;

    @Override
    public BigDecimal getExchangeRate(Currency from, Currency to) {
        return exchangeRateClient.getExchangeRate(from, to);
    }

    @Override
    public BigDecimal getAmount(BigDecimal amount, Currency from, Currency to) {
        var exchangeRate = getExchangeRate(from, to);
        return amount.multiply(exchangeRate).setScale(2, RoundingMode.DOWN);
    }

}
