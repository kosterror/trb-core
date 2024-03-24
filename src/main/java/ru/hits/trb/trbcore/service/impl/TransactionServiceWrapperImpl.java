package ru.hits.trb.trbcore.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.factory.TransactionServiceFactory;
import ru.hits.trb.trbcore.service.TransactionService;
import ru.hits.trb.trbcore.service.TransactionServiceWrapper;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceWrapperImpl implements TransactionServiceWrapper {

    private final TransactionServiceFactory transactionServiceFactory;

    @Override
    public void process(UUID externalTransactionId, @Valid InitTransactionDto initTransaction) {
        var service = transactionServiceFactory.getTransactionService(initTransaction.getType());
        processTransaction(service, externalTransactionId, initTransaction);
    }

    private void processTransaction(TransactionService service,
                                    UUID externalTransactionId,
                                    InitTransactionDto initTransaction) {
        try {
            var transaction = service.process(externalTransactionId, initTransaction);
            log.info("Process transaction with external transaction id = {} success", externalTransactionId);
            service.onSuccess(transaction);
        } catch (Exception exception) {
            log.error("Process transaction with external transaction id = {} failed",
                    externalTransactionId,
                    exception
            );
            service.onFailed(externalTransactionId);
        }
    }

}
