package ru.hits.trb.trbcore.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;

import java.util.UUID;

@Data
public class InitTransactionDto {

    private UUID payerAccountId;

    private UUID payeeAccountId;

    @NotNull
    @Min(1)
    private long amount;

    @NotNull
    private TransactionType type;

}
