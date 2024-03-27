package ru.hits.trb.trbcore.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;
import ru.hits.trb.trbcore.entity.enumeration.Currency;

import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class NewAccountDto {

    @Schema(description = "Тип счета", example = "DEPOSIT", requiredMode = REQUIRED)
    @NotNull
    private AccountType type;

    @Schema(description = "Валюта счета", example = "RUB", requiredMode = REQUIRED)
    @NotNull
    private Currency currency;

    @Schema(description = "ФИО владельца счета", example = "Иванов Иван Иванович", requiredMode = REQUIRED)
    @NotBlank
    private String clientFullName;

    @Schema(description = "Идентификатор пользователя из trb-users", requiredMode = REQUIRED)
    @NotNull
    private UUID externalClientId;

}
