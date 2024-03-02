package ru.hits.trb.trbcore.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;
import ru.hits.trb.trbcore.exception.InvalidAccountTypeException;
import ru.hits.trb.trbcore.exception.NotFoundException;
import ru.hits.trb.trbcore.mapper.AccountMapper;
import ru.hits.trb.trbcore.repository.AccountRepository;
import ru.hits.trb.trbcore.service.AccountService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountMapper mapper;
    private final AccountRepository repository;

    @Override
    public AccountDto createClientAccount(NewAccountDto dto) {
        if (dto.getType() == AccountType.MASTER) {
            throw new InvalidAccountTypeException("It's impossible to create account with master type");
        }

        var accountEntity = mapper.newDtoToEntity(dto);
        accountEntity = repository.save(accountEntity);

        return mapper.entityToDto(accountEntity);
    }

    @Override
    public AccountDto getAccount(UUID id) {
        return repository
                .findById(id)
                .map(mapper::entityToDto)
                .orElseThrow(() -> new NotFoundException("Account with id '" + id + "' not found"));
    }

}
