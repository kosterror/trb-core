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
import ru.hits.trb.trbcore.dto.transaction.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.exception.MessageDeserializationException;
import ru.hits.trb.trbcore.service.LoanTransactionService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RepaymentConsumer {

    private static final String TOPIC = "${kafka.topic.loan-repayment}";
    private static final String GROUP_ID = "${spring.kafka.consumer.group-id}";

    private final ObjectMapper mapper;
    private final LoanTransactionService loanTransactionService;

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void transactionLoanRepayment(@Header(KafkaHeaders.RECEIVED_KEY) String key,
                                         @Payload String message) {
        try {
            var transactionDto = mapper.readValue(message, UnidirectionalTransactionDto.class);
            var loanRepaymentId = mapper.readValue(key, UUID.class);

            loanTransactionService.makeRepaymentTransaction(loanRepaymentId, transactionDto);
            log.info("Repayment finished successfully");
        } catch (JsonProcessingException exception) {
            throw new MessageDeserializationException("Failed to deserialize message: " + message, exception);
        }
    }

}
