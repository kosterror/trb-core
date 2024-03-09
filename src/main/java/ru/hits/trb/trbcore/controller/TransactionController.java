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
import ru.hits.trb.trbcore.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.dto.transaction.UnidirectionalTransactionDto;
import ru.hits.trb.trbcore.exception.NotEnoughMoneyException;
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

    @PostMapping("/withdrawal")
    @Operation(summary = "Вывести деньги")
    public void withdrawal(
            @Valid @RequestBody UnidirectionalTransactionDto unidirectionalTransactionDto
    ) throws NotEnoughMoneyException {
        transactionService.withdrawal(unidirectionalTransactionDto);
    }

    @PostMapping("/account-to-account")
    @Operation(summary = "Сделать перевод между счетами")
    public TransactionDto accountTransaction(@Valid @RequestBody InitTransactionDto initTransactionDto) {
        return transactionService.makeTransferTransaction(initTransactionDto);
    }

}
