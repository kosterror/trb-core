package ru.hits.trb.trbcore.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.dto.transaction.TransactionCallbackDto;
import ru.hits.trb.trbcore.exception.InternalServiceException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionCallbackProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.producer.transaction-callback}")
    private String topic;

    public void sendMessage(UUID externalTransactionId, TransactionCallbackDto transactionCallback) {
        try {
            var key = objectMapper.writeValueAsString(externalTransactionId);
            var value = objectMapper.writeValueAsString(transactionCallback);

            kafkaTemplate.send(topic, key, value);

            log.info("Transaction callback record sent, key: {}, value {}", key, value);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException("Failed to serialize data for kafka record", exception);
        }
    }

}