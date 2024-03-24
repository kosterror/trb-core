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
import ru.hits.trb.trbcore.exception.NotEnoughMoneyException;
import ru.hits.trb.trbcore.mapper.TransactionMapper;
import ru.hits.trb.trbcore.producer.TransactionCallbackProducer;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.repository.TransactionRepository;
import ru.hits.trb.trbcore.service.AccountService;
import ru.hits.trb.trbcore.service.TransactionService;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class WithdrawalTransactionService implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final TransactionMapper transactionMapper;
    private final TransactionCallbackProducer transactionCallbackProducer;

    @Override
    @Transactional
    public TransactionDto process(UUID externalTransactionId, InitTransactionDto initTransaction) {
        var account = accountService.findAccount(initTransaction.getPayerAccountId());
        var balance = account.getBalance();

        if (balance < initTransaction.getAmount()) {
            throw new NotEnoughMoneyException(
                    StringTemplate.STR."Account \{account.getId()} has only \{account.getBalance()} but it needs \{initTransaction.getAmount()}"
            );
        }

        var transaction = TransactionEntity.builder()
                .externalId(externalTransactionId)
                .date(new Date())
                .payerAccountId(account.getId())
                .amount(initTransaction.getAmount())
                .type(TransactionType.WITHDRAWAL)
                .build();

        account.setBalance(balance - transaction.getAmount());

        accountRepository.save(account);
        transaction = transactionRepository.save(transaction);

        log.info("Withdrawal transaction from {} with amount {} was completed successfully",
                account.getId(),
                transaction.getAmount()
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
