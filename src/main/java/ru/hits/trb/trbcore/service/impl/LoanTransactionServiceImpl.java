package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbcore.dto.transaction.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.producer.PaymentCallbackProducer;
import ru.hits.trb.trbcore.producer.RepaymentCallbackProducer;
import ru.hits.trb.trbcore.service.LoanTransactionService;
import ru.hits.trb.trbcore.service.TransactionService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanTransactionServiceImpl implements LoanTransactionService {

    private final TransactionService transactionService;
    private final RepaymentCallbackProducer repaymentCallbackProducer;
    private final PaymentCallbackProducer paymentCallbackProducer;

    @Override
    @Transactional
    public void makeRepaymentTransaction(UUID loanRepaymentId,
                                         UnidirectionalTransactionDto unidirectionalTransactionDto
    ) {
        var transaction = transactionService.repayment(unidirectionalTransactionDto);
        log.info("Loan repayment {} finished successfully", loanRepaymentId);

        repaymentCallbackProducer.sendMessage(loanRepaymentId, transaction.getState());
    }

    @Override
    @Transactional
    public void makePaymentTransaction(UUID loanPaymentId,
                                       UnidirectionalTransactionDto unidirectionalTransactionDto
    ) {
        var transaction = transactionService.payment(unidirectionalTransactionDto);
        log.info("Loan payment {} finished successfully", loanPaymentId);

        paymentCallbackProducer.sendMessage(loanPaymentId, transaction.getState());
    }

}
