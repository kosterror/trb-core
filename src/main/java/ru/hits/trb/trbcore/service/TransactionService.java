package ru.hits.trb.trbcore.service;

import java.util.UUID;

public interface TransactionService {

    void makeTransferTransaction(UUID payerAccountId, UUID payeeAccountId, long amountInKopecks);

}
