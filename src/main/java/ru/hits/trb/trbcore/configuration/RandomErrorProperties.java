package ru.hits.trb.trbcore.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "random-error")
public class RandomErrorProperties {

    private final boolean isEnable;
    private final double probabilityAverage;
    private final double probabilityExtreme;
    private final int extremePeriodDurationMin;
    private final double extremePeriodFrequencyMin;

}
