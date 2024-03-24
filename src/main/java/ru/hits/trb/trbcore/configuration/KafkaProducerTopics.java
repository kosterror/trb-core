package ru.hits.trb.trbcore.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka.topic.producer")
public class KafkaProducerTopics {

    private String transactionCallback;

    private String loanPaymentCallback;

    private String loanRepaymentCallback;

}
