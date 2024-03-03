package ru.hits.trb.trbcore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.hits.trb.trbcore.dto.PaginationResponse;
import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.service.AccountService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "API для счетов")
public class AccountController {

    private final AccountService service;

    @PostMapping("/accounts")
    @Operation(summary = "Создать счет для клиента")
    public AccountDto createClientAccount(@Valid @RequestBody NewAccountDto dto) {
        return service.createClientAccount(dto);
    }

    @GetMapping("/accounts/{id}")
    @Operation(summary = "Получить информацию о счете по id")
    public AccountDto getAccount(@PathVariable UUID id) {
        return service.getAccount(id);
    }

    @DeleteMapping("/accounts/{id}")
    @Operation(summary = "Закрыть счет")
    public void closeAccount(@PathVariable UUID id) {
        service.closeAccount(id);
    }

    @GetMapping("/users/{id}/accounts")
    @Operation(summary = "Получить счета клиента")
    public List<AccountDto> getUserAccounts(@PathVariable UUID id) {
        return service.getUserAccounts(id);
    }

    @GetMapping("/accounts/{accountId}/history")
    @Operation(summary = "Получить историю по счету")
    public PaginationResponse<TransactionDto> getHistory(@PathVariable UUID accountId,
                                                         @RequestParam @Min(0) int page,
                                                         @RequestParam @Min(1) @Max(200) int size) {
        return service.getHistory(accountId, page, size);
    }

}
