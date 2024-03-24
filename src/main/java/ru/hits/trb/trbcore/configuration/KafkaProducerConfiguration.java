package ru.hits.trb.trbcore.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration {

    private final KafkaProducerTopics kafkaProducerTopics;

    @Bean
    public NewTopic transactionCallback() {
        return TopicBuilder.name(kafkaProducerTopics.getTransactionCallback())
                .build();
    }

    @Bean
    public NewTopic loanPaymentCallback() {
        return TopicBuilder.name(kafkaProducerTopics.getLoanPaymentCallback())
                .build();
    }

    @Bean
    public NewTopic loanRepaymentCallback() {
        return TopicBuilder.name(kafkaProducerTopics.getLoanRepaymentCallback())
                .build();
    }

}