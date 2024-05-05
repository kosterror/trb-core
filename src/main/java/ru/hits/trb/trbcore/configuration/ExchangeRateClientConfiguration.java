package ru.hits.trb.trbcore.configuration;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.observation.DefaultClientRequestObservationConvention;
import org.springframework.web.client.RestClient;

@Configuration
public class ExchangeRateClientConfiguration {

    @Value("${urls.exchange-rate}")
    private String url;

    @Bean
    public RestClient exchangeRateClient(ObservationRegistry observationRegistry) {
        return RestClient.builder()
                .baseUrl(url)
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention())
                .build();
    }

}
