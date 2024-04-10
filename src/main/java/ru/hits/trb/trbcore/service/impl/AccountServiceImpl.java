package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.dto.PaginationResponse;
import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.entity.AccountEntity;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;
import ru.hits.trb.trbcore.entity.enumeration.Currency;
import ru.hits.trb.trbcore.exception.InvalidAccountTypeException;
import ru.hits.trb.trbcore.exception.NotFoundException;
import ru.hits.trb.trbcore.mapper.AccountMapper;
import ru.hits.trb.trbcore.mapper.TransactionMapper;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.repository.TransactionRepository;
import ru.hits.trb.trbcore.service.AccountService;
import ru.hits.trb.trbcore.service.ExchangeRateService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ExchangeRateService exchangeRateService;

    @Override
    public AccountDto createClientAccount(NewAccountDto dto) {
        if (dto.getType() == AccountType.MASTER) {
            throw new InvalidAccountTypeException("It's impossible to create account with master type");
        }

        var accountEntity = accountMapper.newDtoToEntity(dto);
        accountEntity = accountRepository.save(accountEntity);

        return accountMapper.entityToDto(accountEntity);
    }

    @Override
    public AccountDto getAccount(UUID id) {
        var account = findAccount(id);

        return accountMapper.entityToDto(account);
    }

    @Override
    public void closeAccount(UUID id) {
        var account = findAccount(id);
        account.setIsClosed(true);

        accountRepository.save(account);
    }

    @Override
    public AccountEntity findAccount(UUID id) {
        return accountRepository
                .findByIdAndIsClosed(id, false)
                .orElseThrow(() -> new NotFoundException(STR."Account with id '\{id}' not found"));
    }

    @Override
    public AccountEntity findMasterAccountWithAmount(BigDecimal fromAmount, Currency fromCurrency) {
        Map<Currency, BigDecimal> currencyAmountMap = new HashMap<>();

        for (var currency : Currency.values()) {
            var amount = exchangeRateService.getExchangeRate(fromCurrency, currency);
            currencyAmountMap.put(currency, amount);
        }

        AccountEntity account = null;

        for (var entry : currencyAmountMap.entrySet()) {
            var accountOptional = accountRepository.findFirstByBalanceIsGreaterThanEqualAndCurrency(
                    entry.getValue(),
                    entry.getKey()
            );

            if (accountOptional.isPresent()) {
                account = accountOptional.get();
                break;
            }
        }

        if (account == null) {
            throw new NotFoundException(STR."Master account with amount \{fromAmount} of \{fromCurrency} was not found");
        }

        return account;
    }

    @Override
    public List<AccountDto> getUserAccounts(UUID id) {
        return accountRepository
                .findAllByExternalClientIdAndIsClosedOrderByCreationDate(id, false)
                .stream()
                .map(accountMapper::entityToDto)
                .toList();
    }

    @Override
    public PaginationResponse<TransactionDto> getHistory(UUID accountId, int pageNumber, int sizeNumber) {
        var pageRequest = PageRequest.of(pageNumber, sizeNumber, Sort.Direction.ASC, "date");
        var page = transactionRepository.findAllByPayeeAccountIdOrPayerAccountId(pageRequest, accountId, accountId);
        var transactions = page.stream().map(transactionMapper::entityToDto).toList();

        return PaginationResponse.<TransactionDto>builder()
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .elements(transactions)
                .build();
    }

}
