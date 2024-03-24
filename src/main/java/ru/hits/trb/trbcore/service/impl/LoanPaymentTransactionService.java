package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.entity.TransactionEntity;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;
import ru.hits.trb.trbcore.mapper.TransactionMapper;
import ru.hits.trb.trbcore.producer.PaymentCallbackProducer;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.repository.TransactionRepository;
import ru.hits.trb.trbcore.service.AccountService;
import ru.hits.trb.trbcore.service.TransactionService;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPaymentTransactionService implements TransactionService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final PaymentCallbackProducer paymentCallbackProducer;

    @Override
    public TransactionDto process(UUID externalTransactionId, InitTransactionDto initTransaction) {
        var payeeAccount = accountService.findAccount(initTransaction.getPayeeAccountId());
        var masterAccount = accountService.findMasterAccountWithAmount(initTransaction.getAmount());

        var transaction = TransactionEntity.builder()
                .externalId(externalTransactionId)
                .date(new Date())
                .payerAccountId(masterAccount.getId())
                .payeeAccountId(payeeAccount.getId())
                .amount(initTransaction.getAmount())
                .type(TransactionType.LOAN_PAYMENT)
                .build();

        payeeAccount.setBalance(payeeAccount.getBalance() + initTransaction.getAmount());
        masterAccount.setBalance(masterAccount.getBalance() - initTransaction.getAmount());

        accountRepository.save(payeeAccount);
        accountRepository.save(masterAccount);
        transaction = transactionRepository.save(transaction);
        log.info("Loan payment transaction from {} to {} was completed successfully",
                masterAccount.getId(),
                payeeAccount.getId()
        );

        return transactionMapper.entityToDto(transaction);
    }

    @Override
    public void onSuccess(TransactionDto transaction) {
        paymentCallbackProducer.sendMessage(transaction.getExternalId(), TransactionState.DONE);
    }

    @Override
    public void onFailed(UUID externalTransactionId) {
        paymentCallbackProducer.sendMessage(externalTransactionId, TransactionState.REJECTED);
    }

}