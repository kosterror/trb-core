package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.entity.TransactionEntity;
import ru.hits.trb.trbcore.entity.enumeration.TransactionCode;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.repository.TransactionRepository;
import ru.hits.trb.trbcore.service.TransactionService;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Override
    public void makeTransferTransaction(UUID payerAccountId,
                                        UUID payeeAccountId,
                                        long amount
    ) {
        if (amount <= 0) {
            log.info("Invalid amount of transaction: {}", amount);
            saveRejectedTransaction(payerAccountId, payeeAccountId, amount, TransactionCode.INVALID_AMOUNT);
            return;
        }

        var payerAccountOptional = accountRepository.findById(payerAccountId);
        var payeeAccountOptional = accountRepository.findById(payeeAccountId);

        if (payerAccountOptional.isEmpty()) {
            log.info("Account of payer with id {} not found", payerAccountId);
            saveRejectedTransaction(payerAccountId, payeeAccountId, amount, TransactionCode.ACCOUNT_PAYER_NOT_FOUND);
            return;
        }

        if (payeeAccountOptional.isEmpty()) {
            log.info("Account of payee with id {} not found", payeeAccountId);
            saveRejectedTransaction(payerAccountId, payeeAccountId, amount, TransactionCode.ACCOUNT_PAYEE_NOT_FOUND);
            return;
        }

        var payerAccount = payerAccountOptional.get();
        var payeeAccount = payeeAccountOptional.get();

        if (payerAccount.isClosed()) {
            log.info("Payer account with id {} is blocked", payerAccountId);
            saveRejectedTransaction(payerAccountId, payeeAccountId, amount, TransactionCode.ACCOUNT_PAYER_CLOSED);
            return;
        }

        if (payeeAccount.isClosed()) {
            log.info("Payee account with id {} is blocked", payeeAccountId);
            saveRejectedTransaction(payerAccountId, payeeAccountId, amount, TransactionCode.ACCOUNT_PAYEE_CLOSED);
            return;
        }

        if (payerAccount.getBalance() < amount) {
            log.info("Payer does not have enough money for transfer");
            saveRejectedTransaction(payerAccountId, payeeAccountId, amount, TransactionCode.NOT_ENOUGH_MONEY);
            return;
        }

        var transaction = TransactionEntity.Builder()
                .date(new Date())
                .payerAccountId(payerAccountId)
                .payeeAccountId(payeeAccountId)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .state(TransactionState.DONE)
                .code(TransactionCode.SUCCESS.getDbCode())
                .build();

        payerAccount.setBalance(payerAccount.getBalance() - amount);
        payeeAccount.setBalance(payeeAccount.getBalance() + amount);

        accountRepository.save(payeeAccount);
        accountRepository.save(payerAccount);
        transactionRepository.save(transaction);
    }

    private void saveRejectedTransaction(UUID payerAccountId,
                                         UUID payeeAccountId,
                                         long amount,
                                         TransactionCode code
    ) {
        var transaction = TransactionEntity.Builder()
                .date(new Date())
                .payerAccountId(payerAccountId)
                .payeeAccountId(payeeAccountId)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .state(TransactionState.REJECTED)
                .code(code.getDbCode())
                .build();

        transactionRepository.save(transaction);
    }

}
