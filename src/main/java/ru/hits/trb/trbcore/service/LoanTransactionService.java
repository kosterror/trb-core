package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.transaction.UnidirectionalTransactionDto;

import java.util.UUID;

public interface LoanTransactionService {

    void makeRepaymentTransaction(UUID loanRepaymentId, UnidirectionalTransactionDto unidirectionalTransactionDto);

    void makePaymentTransaction(UUID loanPaymentId, UnidirectionalTransactionDto unidirectionalTransactionDto);
}
