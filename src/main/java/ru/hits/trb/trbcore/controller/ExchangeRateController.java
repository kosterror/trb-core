package ru.hits.trb.trbcore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.trb.trbcore.entity.enumeration.Currency;
import ru.hits.trb.trbcore.service.ExchangeRateService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/exchange-rate")
@RequiredArgsConstructor
@Tag(name = "API для курса валют")
public class ExchangeRateController {

    private final ExchangeRateService exchangeRateService;

    @GetMapping("/{from}/{to}")
    @Operation(summary = "Получить обменный курс валют")
    public BigDecimal getCurrency(@PathVariable @NotNull Currency from,
                                  @PathVariable @NotNull Currency to) {
        return exchangeRateService.getExchangeRate(from, to);
    }

}
