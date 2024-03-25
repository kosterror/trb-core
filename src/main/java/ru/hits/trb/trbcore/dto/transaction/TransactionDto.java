package ru.hits.trb.trbcore.dto.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.hits.trb.trbcore.entity.enumeration.Currency;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class TransactionDto {

    @Schema(description = "Идентификатор транзакции", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "Внешний идентификатор транзакции, задается сервисом, который инициирует транзакцию",
            requiredMode = REQUIRED
    )
    private UUID externalId;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(description = "Дата проведения транзакции",
            requiredMode = REQUIRED,
            example = "1709372199882",
            type = "string")
    private Date date;

    @Schema(description = "Отправитель", requiredMode = NOT_REQUIRED)
    private UUID payerAccountId;

    @Schema(description = "Получатель", requiredMode = NOT_REQUIRED)
    private UUID payeeAccountId;

    @Schema(description = "Сумма", requiredMode = REQUIRED)
    private BigDecimal amount;

    @Schema(description = "Валюта", requiredMode = REQUIRED)
    private Currency currency;

    @Schema(description = "Тип транзакции", requiredMode = REQUIRED)
    private TransactionType type;

}
