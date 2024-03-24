package ru.hits.trb.trbcore.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.exception.InternalServiceException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCallbackProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.producer.loan-payment-callback}")
    private String topic;

    public void sendMessage(UUID loanPaymentId, TransactionState transactionState) {
        try {
            var key = objectMapper.writeValueAsString(loanPaymentId);
            var value = objectMapper.writeValueAsString(transactionState);

            kafkaTemplate.send(topic, key, value);

            log.info("Payment callback record sent, id: {}, transaction state {}", key, value);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException("Failed to serialize data for kafka record", exception);
        }
    }

}