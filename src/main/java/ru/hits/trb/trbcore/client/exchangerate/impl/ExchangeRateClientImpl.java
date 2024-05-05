package ru.hits.trb.trbcore.client.exchangerate.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;
import ru.hits.trb.trbcore.client.exchangerate.ExchangeRateClient;
import ru.hits.trb.trbcore.entity.enumeration.Currency;

import java.math.BigDecimal;

@Slf4j
@Service
public class ExchangeRateClientImpl implements ExchangeRateClient {

    private final RestClient restClient;

    public ExchangeRateClientImpl(@Qualifier("exchangeRateClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    @Retryable(retryFor = HttpServerErrorException.class, maxAttempts = 15)
    public BigDecimal getExchangeRate(Currency from, Currency to) {
        log.info("Getting exchange rate from {} to {}", from, to);

        var exchangeRate = restClient.get()
                .uri(builder -> builder
                        .path(Paths.GET_EXCHANGE_RATE)
                        .build(from.toString(), to.toString())
                ).retrieve()
                .toEntity(BigDecimal.class)
                .getBody();

        log.info("Exchange rate received: {}", exchangeRate);

        return exchangeRate;
    }

}
