package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;

import java.util.UUID;

public interface TransactionServiceWrapper {

    void process(UUID externalTransactionId, InitTransactionDto transactionDto);

}
