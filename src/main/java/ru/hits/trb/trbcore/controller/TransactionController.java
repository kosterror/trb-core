package ru.hits.trb.trbcore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.trb.trbcore.dto.account.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.service.TransactionService;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Tag(name = "API для счетов")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/replenishment")
    @Operation(summary = "Пополнить счет")
    public void replenishment(@Valid @RequestBody UnidirectionalTransactionDto unidirectionalTransactionDto) {
        transactionService.replenishment(unidirectionalTransactionDto);
    }

}
