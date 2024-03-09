package ru.hits.trb.trbcore.dto.transaction;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class UnidirectionalTransactionDto {

    @NotNull
    private UUID accountId;

    @Min(1)
    private long amount;

}
