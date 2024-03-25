package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplenishmentTransactionService implements TransactionService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionCallbackProducer transactionCallbackProducer;
    private final ExchangeRateService exchangeRateService;


    @Override
    public TransactionDto process(UUID externalTransactionId, InitTransactionDto initTransaction) {
        var account = accountService.findAccount(initTransaction.getPayeeAccountId());
        var balance = account.getBalance();

        var transaction = TransactionEntity.builder()
                .externalId(externalTransactionId)
                .date(new Date())
                .payeeAccountId(account.getId())
                .amount(initTransaction.getAmount())
                .currency(initTransaction.getCurrency())
                .type(TransactionType.REPLENISHMENT)
                .build();

        var payeeAmount = exchangeRateService.getAmount(initTransaction.getAmount(),
                initTransaction.getCurrency(),
                account.getCurrency()
        );

        account.setBalance(balance.add(payeeAmount));

        accountRepository.save(account);
        transaction = transactionRepository.save(transaction);

        log.info("Replenishment transaction from {} with amount {} and currency {} was completed successfully",
                account.getId(),
                transaction.getAmount(),
                transaction.getCurrency()
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
