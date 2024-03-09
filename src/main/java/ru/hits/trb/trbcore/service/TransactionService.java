package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.dto.transaction.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.entity.TransactionEntity;
import ru.hits.trb.trbcore.exception.NotEnoughMoneyException;

public interface TransactionService {

    TransactionDto makeTransferTransaction(InitTransactionDto initTransactionDto);

    void replenishment(UnidirectionalTransactionDto unidirectionalTransactionDto);

    void withdrawal(UnidirectionalTransactionDto unidirectionalTransactionDto) throws NotEnoughMoneyException;

    TransactionEntity repayment(UnidirectionalTransactionDto unidirectionalTransactionDto);
}
