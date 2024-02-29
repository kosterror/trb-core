package ru.hits.trb.trbcore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "API для счетов")
public class AccountController {

    private final AccountService service;

    @PostMapping
    @Operation(summary = "Создать счет для клиента")
    public AccountDto createClientAccount(@Valid @RequestBody NewAccountDto dto) {
        return service.createClientAccount(dto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить информацию о счете по id")
    public AccountDto getAccount(@PathVariable UUID id) {
        return service.getAccount(id);
    }

}
