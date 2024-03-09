package ru.hits.trb.trbcore.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class InitTransactionDto {

    @NotNull
    private UUID payerAccountId;

    @NotNull
    private UUID payeeAccountId;

    @NotNull
    @Min(1)
    private long amount;

}
