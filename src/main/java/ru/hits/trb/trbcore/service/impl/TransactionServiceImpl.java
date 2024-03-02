package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbcore.dto.account.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.entity.TransactionEntity;
import ru.hits.trb.trbcore.entity.enumeration.TransactionCode;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;
import ru.hits.trb.trbcore.exception.BadRequestException;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.repository.TransactionRepository;
import ru.hits.trb.trbcore.service.AccountService;
import ru.hits.trb.trbcore.service.TransactionService;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Override
    @Transactional
    public void makeTransferTransaction(UUID payerAccountId,
                                        UUID payeeAccountId,
                                        long amount
    ) {
        if (amount <= 0) {
            throw new BadRequestException("Invalid amount of transaction: " + amount);
        }

        var payerAccount = accountService.findAccount(payerAccountId);
        var payeeAccount = accountService.findAccount(payeeAccountId);

        if (payerAccount.getBalance() < amount) {
            log.error("Payer does not have enough money for transfer");
            saveNotEnoughMoneyTransaction(payerAccountId, payeeAccountId, amount);
            log.error("Saved rejected transaction");
            return;
        }

        var transaction = TransactionEntity.builder()
                .date(new Date())
                .payerAccountId(payerAccountId)
                .payeeAccountId(payeeAccountId)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .state(TransactionState.DONE)
                .code(TransactionCode.SUCCESS)
                .build();

        payerAccount.setBalance(payerAccount.getBalance() - amount);
        payeeAccount.setBalance(payeeAccount.getBalance() + amount);

        accountRepository.save(payeeAccount);
        accountRepository.save(payerAccount);
        transactionRepository.save(transaction);
        log.info("Transfer transaction from {} to {} was completed successfully", payerAccountId, payeeAccount);
    }

    @Override
    @Transactional
    public void replenishment(UnidirectionalTransactionDto transactionDto) {
        var account = accountService.findAccount(transactionDto.getAccountId());

        var transaction = TransactionEntity.builder()
                .date(new Date())
                .payeeAccountId(account.getId())
                .amount(transactionDto.getAmount())
                .type(TransactionType.REPLENISHMENT)
                .state(TransactionState.DONE)
                .code(TransactionCode.SUCCESS)
                .build();

        account.setBalance(account.getBalance() + transactionDto.getAmount());

        transactionRepository.save(transaction);
        accountRepository.save(account);
        log.info("The replenishment of the account {} was completed successfully", account.getId());
    }

    private void saveNotEnoughMoneyTransaction(UUID payerAccountId,
                                               UUID payeeAccountId,
                                               long amount
    ) {
        var transaction = TransactionEntity.builder()
                .date(new Date())
                .payerAccountId(payerAccountId)
                .payeeAccountId(payeeAccountId)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .state(TransactionState.REJECTED)
                .code(TransactionCode.NOT_ENOUGH_MONEY)
                .build();

        transactionRepository.save(transaction);
    }

}
