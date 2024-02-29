package ru.hits.trb.trbcore.dto.account;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;

import java.util.Date;
import java.util.UUID;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class AccountDto {

    @Schema(description = "Идентификатор счета", requiredMode = REQUIRED)
    private UUID id;

    @Schema(description = "Тип банковского счета", requiredMode = REQUIRED)
    private AccountType type;

    @Schema(description = "Баланс счета", requiredMode = REQUIRED)
    private long balance;

    @Schema(description = "ФИО владельца счета", requiredMode = REQUIRED)
    private String clientFullName;

    @Schema(description = "Внешний идентификатор пользователя из trb-users", requiredMode = REQUIRED)
    private String externalClientId;

    @Schema(description = "Дата создания счета", requiredMode = REQUIRED)
    private Date creationDate;

    @Schema(description = "Дата закрытия счета", requiredMode = NOT_REQUIRED)
    private Date closingDate;

    @Schema(description = "Признак закрытия счета", requiredMode = REQUIRED)
    private boolean isClosed;

}
