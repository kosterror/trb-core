package ru.hits.trb.trbcore.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;

import java.util.UUID;

@Data
public class NewAccountDto {

    @Schema(description = "Тип счета", example = "DEPOSIT")
    private AccountType type;

    @Schema(description = "ФИО владельца счета", example = "Иванов Иван Иванович")
    private String clientFullName;

    @Schema(description = "Идентификатор пользователя из trb-users")
    private UUID externalClientId;

}
