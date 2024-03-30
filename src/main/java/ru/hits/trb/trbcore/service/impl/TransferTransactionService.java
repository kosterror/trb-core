package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionCallbackDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.entity.TransactionEntity;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;
import ru.hits.trb.trbcore.mapper.TransactionMapper;
import ru.hits.trb.trbcore.producer.TransactionCallbackProducer;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.repository.TransactionRepository;
import ru.hits.trb.trbcore.service.AccountService;
import ru.hits.trb.trbcore.service.ExchangeRateService;
import ru.hits.trb.trbcore.service.TransactionService;
import ru.hits.trb.trbcore.util.BalanceValidator;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferTransactionService implements TransactionService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionCallbackProducer transactionCallbackProducer;
    private final ExchangeRateService exchangeRateService;

    @Override
    @Transactional
    public TransactionDto process(UUID externalTransactionId, InitTransactionDto initTransaction) {
        var payeeAccount = accountService.findAccount(initTransaction.getPayeeAccountId());
        var payerAccount = accountService.findAccount(initTransaction.getPayerAccountId());

        var payeeAmount = exchangeRateService.getAmount(initTransaction, payeeAccount);
        var payerAmount = exchangeRateService.getAmount(initTransaction, payerAccount);

        BalanceValidator.validateBalanceForTransaction(payerAccount, payerAmount);

        var transaction = TransactionEntity.builder()
                .externalId(externalTransactionId)
                .date(new Date())
                .payerAccountId(payerAccount.getId())
                .payeeAccountId(payeeAccount.getId())
                .amount(initTransaction.getAmount())
                .currency(initTransaction.getCurrency())
                .type(TransactionType.TRANSFER)
                .build();

        payerAccount.setBalance(payerAccount.getBalance().subtract(payerAmount));
        payeeAccount.setBalance(payeeAccount.getBalance().add(payeeAmount));

        accountRepository.save(payerAccount);
        accountRepository.save(payeeAccount);
        transaction = transactionRepository.save(transaction);
        log.info("Transfer transaction from {} to {} was completed successfully",
                payerAccount.getId(),
                payeeAccount.getId()
        );

        return transactionMapper.entityToDto(transaction);
    }

    @Override
    public void onSuccess(TransactionDto transaction) {
        transactionCallbackProducer.sendMessage(transaction.getExternalId(),
                TransactionCallbackDto.builder()
                        .state(TransactionState.DONE)
                        .transaction(transaction)
                        .build());
    }

    @Override
    public void onFailed(UUID externalTransactionId) {
        transactionCallbackProducer.sendMessage(
                externalTransactionId,
                TransactionCallbackDto.builder()
                        .state(TransactionState.REJECTED)
                        .build()
        );
    }

}

