package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.dto.transaction.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.entity.TransactionEntity;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;
import ru.hits.trb.trbcore.entity.enumeration.TransactionCode;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;
import ru.hits.trb.trbcore.exception.NotEnoughMoneyException;
import ru.hits.trb.trbcore.mapper.TransactionMapper;
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
    private final TransactionMapper transactionMapper;

    @Override
    @Transactional
    public TransactionDto makeTransferTransaction(InitTransactionDto initTransactionDto) {
        var payerAccount = accountService.findAccount(initTransactionDto.getPayerAccountId());
        var payeeAccount = accountService.findAccount(initTransactionDto.getPayeeAccountId());

        if (payerAccount.getBalance() < initTransactionDto.getAmount()) {
            var transaction = saveNotEnoughMoneyTransaction(payerAccount.getId(),
                    payeeAccount.getId(),
                    initTransactionDto.getAmount(),
                    TransactionType.TRANSFER
            );
            log.error("Saved rejected transaction");

            return transactionMapper.entityToDto(transaction);
        }

        var transaction = TransactionEntity.builder()
                .date(new Date())
                .payerAccountId(payerAccount.getId())
                .payeeAccountId(payeeAccount.getId())
                .amount(initTransactionDto.getAmount())
                .type(TransactionType.TRANSFER)
                .state(TransactionState.DONE)
                .code(TransactionCode.SUCCESS)
                .build();

        payerAccount.setBalance(payerAccount.getBalance() - initTransactionDto.getAmount());
        payeeAccount.setBalance(payeeAccount.getBalance() + initTransactionDto.getAmount());

        accountRepository.save(payeeAccount);
        accountRepository.save(payerAccount);
        transaction = transactionRepository.save(transaction);
        log.info("Transfer transaction from {} to {} was completed successfully",
                payerAccount.getId(),
                payeeAccount.getId()
        );

        return transactionMapper.entityToDto(transaction);
    }

    @Override
    @Transactional
    public void replenishment(UnidirectionalTransactionDto transactionDto) {
        var account = accountService.findAccount(transactionDto.getAccountId());

        var transaction = buildTransaction(transactionDto, null, account.getId(), TransactionType.REPLENISHMENT);

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
        var transaction = buildTransaction(transactionDto, account.getId(), null, TransactionType.REPLENISHMENT);

        accountRepository.save(account);
        transactionRepository.save(transaction);
    }

    @Override
    public TransactionEntity repayment(UnidirectionalTransactionDto transactionDto) {
        var loanAccount = accountService.findAccount(transactionDto.getAccountId());
        var masterAccountOptional = accountRepository.findByType(AccountType.MASTER);

        if (masterAccountOptional.isEmpty()) {
            var rejectedTransaction = buildTransaction(transactionDto,
                    loanAccount.getId(),
                    null,
                    TransactionType.LOAN_REPAYMENT
            );

            rejectedTransaction = transactionRepository.save(rejectedTransaction);

            log.info("Master account not found. Saved rejected transaction");

            return rejectedTransaction;
        }

        var masterAccount = masterAccountOptional.get();

        if (loanAccount.getBalance() < transactionDto.getAmount()) {
            var rejectedTransaction = saveNotEnoughMoneyTransaction(loanAccount.getId(),
                    masterAccount.getId(),
                    transactionDto.getAmount(),
                    TransactionType.LOAN_REPAYMENT
            );

            log.error("Not enough money for repayment. Saved rejected transaction");

            return rejectedTransaction;
        }

        loanAccount.setBalance(loanAccount.getBalance() - transactionDto.getAmount());
        masterAccount.setBalance(masterAccount.getBalance() + transactionDto.getAmount());
        var transaction = buildTransaction(transactionDto,
                loanAccount.getId(),
                masterAccount.getId(),
                TransactionType.LOAN_REPAYMENT
        );

        accountRepository.save(loanAccount);
        accountRepository.save(masterAccount);
        return transactionRepository.save(transaction);
    }

    @Override
    public TransactionEntity payment(UnidirectionalTransactionDto transactionDto) {
        var loanAccount = accountService.findAccount(transactionDto.getAccountId());
        var masterAccountOptional = accountRepository.findByType(AccountType.MASTER);

        if (masterAccountOptional.isEmpty()) {
            var rejectedTransaction = buildTransaction(transactionDto,
                    null,
                    loanAccount.getId(),
                    TransactionType.LOAN_PAYMENT
            );

            rejectedTransaction = transactionRepository.save(rejectedTransaction);

            log.info("Master account not found. Saved rejected transaction");

            return rejectedTransaction;
        }

        var masterAccount = masterAccountOptional.get();

        loanAccount.setBalance(loanAccount.getBalance() + transactionDto.getAmount());
        masterAccount.setBalance(masterAccount.getBalance() - transactionDto.getAmount());
        var transaction = buildTransaction(transactionDto,
                masterAccount.getId(),
                loanAccount.getId(),
                TransactionType.LOAN_PAYMENT
        );

        accountRepository.save(loanAccount);
        accountRepository.save(masterAccount);
        return transactionRepository.save(transaction);
    }

    private TransactionEntity saveNotEnoughMoneyTransaction(UUID payerAccountId,
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

        return transactionRepository.save(transaction);
    }

    private TransactionEntity buildTransaction(UnidirectionalTransactionDto transactionDto,
                                               UUID payerAccountId,
                                               UUID payeeAccountId,
                                               TransactionType type
    ) {
        return TransactionEntity.builder()
                .date(new Date())
                .payerAccountId(payerAccountId)
                .payeeAccountId(payeeAccountId)
                .amount(transactionDto.getAmount())
                .type(type)
                .state(TransactionState.DONE)
                .code(TransactionCode.SUCCESS)
                .build();
    }

}
