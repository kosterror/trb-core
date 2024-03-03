package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.account.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.exception.NotEnoughMoney;

import java.util.UUID;

public interface TransactionService {

    void makeTransferTransaction(UUID payerAccountId,
                                 UUID payeeAccountId,
                                 long amountInKopecks
    ) throws NotEnoughMoney;

    void replenishment(UnidirectionalTransactionDto unidirectionalTransactionDto);

    void withdrawal(UnidirectionalTransactionDto unidirectionalTransactionDto) throws NotEnoughMoney;
}
