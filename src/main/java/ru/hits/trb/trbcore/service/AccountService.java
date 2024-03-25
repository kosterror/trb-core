package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.PaginationResponse;
import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.entity.AccountEntity;
import ru.hits.trb.trbcore.entity.enumeration.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountDto createClientAccount(NewAccountDto dto);

    AccountDto getAccount(UUID id);

    void closeAccount(UUID id);

    AccountEntity findAccount(UUID id);

    AccountEntity findMasterAccountWithAmount(BigDecimal amount, Currency currency);

    List<AccountDto> getUserAccounts(UUID id);

    PaginationResponse<TransactionDto> getHistory(UUID accountId, int page, int size);
}
