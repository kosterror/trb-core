package ru.hits.trb.trbcore.dto.transaction;

import lombok.Builder;
import lombok.Data;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;

@Data
@Builder
public class TransactionCallbackDto {

    private TransactionState state;

    private TransactionDto transaction;

}
