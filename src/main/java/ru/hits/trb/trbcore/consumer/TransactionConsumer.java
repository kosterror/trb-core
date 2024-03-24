package ru.hits.trb.trbcore.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.exception.MessageDeserializationException;
import ru.hits.trb.trbcore.service.TransactionServiceWrapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionConsumer {

    private static final String TOPIC = "${kafka.topic.consumer.transaction-initialization}";
    private static final String GROUP_ID = "${spring.kafka.consumer.group-id}";

    private final ObjectMapper mapper;
    private final TransactionServiceWrapper service;

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void makeTransaction(@Header(KafkaHeaders.RECEIVED_KEY) String key,
                                @Payload String message) {
        try {
            var initTransaction = mapper.readValue(message, InitTransactionDto.class);
            var externalTransactionId = mapper.readValue(key, UUID.class);

            service.process(externalTransactionId, initTransaction);
            log.info("Transaction finished success");
        } catch (JsonProcessingException exception) {
            throw new MessageDeserializationException(STR."Failed to deserialize key \{key} or message: \{message}",
                    exception
            );
        }
    }

}
