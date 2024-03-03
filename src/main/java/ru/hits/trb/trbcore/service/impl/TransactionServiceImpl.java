package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbcore.dto.account.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.entity.AccountEntity;
import ru.hits.trb.trbcore.entity.TransactionEntity;
import ru.hits.trb.trbcore.entity.enumeration.TransactionCode;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;
import ru.hits.trb.trbcore.exception.BadRequestException;
import ru.hits.trb.trbcore.exception.NotEnoughMoneyException;
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
    ) throws NotEnoughMoneyException {
        if (amount <= 0) {
            throw new BadRequestException("Invalid amount of transaction: " + amount);
        }

        var payerAccount = accountService.findAccount(payerAccountId);
        var payeeAccount = accountService.findAccount(payeeAccountId);

        if (payerAccount.getBalance() < amount) {
            saveNotEnoughMoneyTransaction(payerAccountId, payeeAccountId, amount, TransactionType.TRANSFER);
            log.error("Saved rejected transaction");
            throw new NotEnoughMoneyException("Not enough money for transfer. Payer: "
                    + payeeAccountId
                    + ", payee: "
                    + payeeAccountId
                    + ", amount: "
                    + amount);
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

        var transaction = buildTransaction(transactionDto, account);

        account.setBalance(account.getBalance() + transactionDto.getAmount());

        transactionRepository.save(transaction);
        accountRepository.save(account);
        log.info("The replenishment of the account {} was completed successfully", account.getId());
    }

    @Override
    @Transactional
    public void withdrawal(UnidirectionalTransactionDto transactionDto) throws NotEnoughMoneyException {
        var account = accountService.findAccount(transactionDto.getAccountId());
        var balance = account.getBalance();

        if (balance < transactionDto.getAmount()) {
            saveNotEnoughMoneyTransaction(account.getId(),
                    null,
                    transactionDto.getAmount(),
                    TransactionType.WITHDRAWAL
            );
            log.error("Not enough money for withdrawal. Saved rejected transaction");
            throw new NotEnoughMoneyException("Not enough money for withdrawal. Account : "
                    + account.getId()
                    + ", amount: "
                    + transactionDto.getAmount()
            );
        }

        account.setBalance(balance - transactionDto.getAmount());
        var transaction = buildTransaction(transactionDto, account);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    private void saveNotEnoughMoneyTransaction(UUID payerAccountId,
                                               UUID payeeAccountId,
                                               long amount,
                                               TransactionType type
    ) {
        var transaction = TransactionEntity.builder()
                .date(new Date())
                .payerAccountId(payerAccountId)
                .payeeAccountId(payeeAccountId)
                .amount(amount)
                .type(type)
                .state(TransactionState.REJECTED)
                .code(TransactionCode.NOT_ENOUGH_MONEY)
                .build();

        transactionRepository.save(transaction);
    }

    private TransactionEntity buildTransaction(UnidirectionalTransactionDto transactionDto,
                                               AccountEntity account
    ) {
        return TransactionEntity.builder()
                .date(new Date())
                .payeeAccountId(account.getId())
                .amount(transactionDto.getAmount())
                .type(TransactionType.REPLENISHMENT)
                .state(TransactionState.DONE)
                .code(TransactionCode.SUCCESS)
                .build();
    }

}
