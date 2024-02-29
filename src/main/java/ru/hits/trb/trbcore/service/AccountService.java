package ru.hits.trb.trbcore.service;

import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;

import java.util.UUID;

public interface AccountService {

    AccountDto createClientAccount(NewAccountDto dto);

    AccountDto getAccount(UUID id);
}
