package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;
import ru.hits.trb.trbcore.exception.ApplicationException;
import ru.hits.trb.trbcore.mapper.AccountMapper;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.service.AccountService;
import ru.hits.trb.trbcore.util.Error;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper mapper;
    private final AccountRepository repository;

    @Override
    public AccountDto createClientAccount(NewAccountDto dto) {
        if (dto.getType() == AccountType.MASTER) {
            throw new ApplicationException(
                    Error.INCORRECT_ACCOUNT_TYPE_WHEN_CREATING,
                    "It's impossible to create master account"
            );
        }

        var accountEntity = mapper.newDtoToEntity(dto);
        accountEntity = repository.save(accountEntity);

        return mapper.entityToDto(accountEntity);
    }

}
