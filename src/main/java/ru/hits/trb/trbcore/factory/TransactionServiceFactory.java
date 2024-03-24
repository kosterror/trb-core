package ru.hits.trb.trbcore.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;
import ru.hits.trb.trbcore.service.TransactionService;

@Service
public class TransactionServiceFactory {

    private final TransactionService transferTransactionService;
    private final TransactionService replenishmentTransactionService;
    private final TransactionService withdrawalTransactionService;
    private final TransactionService loanRepaymentTransactionService;
    private final TransactionService loanPaymentTransactionService;


    @Autowired
    public TransactionServiceFactory(@Qualifier("transferTransactionService") TransactionService
                                             transferTransactionService,
                                     @Qualifier("replenishmentTransactionService") TransactionService
                                             replenishmentTransactionService,
                                     @Qualifier("withdrawalTransactionService") TransactionService
                                             withdrawalTransactionService,
                                     @Qualifier("loanRepaymentTransactionService") TransactionService
                                             loanRepaymentTransactionService,
                                     @Qualifier("loanPaymentTransactionService") TransactionService
                                             loanPaymentTransactionService) {
        this.transferTransactionService = transferTransactionService;
        this.replenishmentTransactionService = replenishmentTransactionService;
        this.withdrawalTransactionService = withdrawalTransactionService;
        this.loanRepaymentTransactionService = loanRepaymentTransactionService;
        this.loanPaymentTransactionService = loanPaymentTransactionService;
    }

    public TransactionService getTransactionService(TransactionType type) {
        return switch (type) {
            case TRANSFER -> transferTransactionService;
            case REPLENISHMENT -> replenishmentTransactionService;
            case WITHDRAWAL -> withdrawalTransactionService;
            case LOAN_REPAYMENT -> loanRepaymentTransactionService;
            case LOAN_PAYMENT -> loanPaymentTransactionService;
        };
    }

}
