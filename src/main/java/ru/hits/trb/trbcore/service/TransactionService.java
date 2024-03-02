package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.account.UnidirectionalTransactionDto;

import java.util.UUID;

public interface TransactionService {

    void makeTransferTransaction(UUID payerAccountId, UUID payeeAccountId, long amountInKopecks);

    void replenishment(UnidirectionalTransactionDto unidirectionalTransactionDto);
}
