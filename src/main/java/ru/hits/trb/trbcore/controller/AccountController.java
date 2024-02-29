package ru.hits.trb.trbcore.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.service.AccountService;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PostMapping
    public AccountDto createClientAccount(@Valid @RequestBody NewAccountDto dto) {
        return service.createClientAccount(dto);
    }

    @GetMapping("/{id}")
    public AccountDto getAccount(@PathVariable UUID id) {
        return service.getAccount(id);
    }

}
