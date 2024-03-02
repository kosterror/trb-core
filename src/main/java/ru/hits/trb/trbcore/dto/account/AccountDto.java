package ru.hits.trb.trbcore.dto.account;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(description = "Дата создания счета",
            requiredMode = REQUIRED,
            example = "1709372199882",
            type = "string")
    private Date creationDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Schema(description = "Дата закрытия счета",
            requiredMode = NOT_REQUIRED,
            example = "1709372199882",
            type = "string")
    private Date closingDate;

    @Schema(description = "Признак закрытия счета", requiredMode = REQUIRED)
    private boolean isClosed;

}
