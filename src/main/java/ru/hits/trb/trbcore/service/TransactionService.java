package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;

import java.util.UUID;

public interface TransactionService {

    TransactionDto process(UUID externalTransactionId, InitTransactionDto transactionDto);

    void onSuccess(TransactionDto transaction);

    void onFailed(UUID externalTransactionId);

}
