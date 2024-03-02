package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.entity.AccountEntity;

import java.util.UUID;

public interface AccountService {

    AccountDto createClientAccount(NewAccountDto dto);

    AccountDto getAccount(UUID id);

    void closeAccount(UUID id);

    AccountEntity findAccount(UUID id);
}
