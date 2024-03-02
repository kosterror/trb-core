package ru.hits.trb.trbcore.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;
import ru.hits.trb.trbcore.entity.enumeration.TransactionState;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;

import java.util.Date;
import java.util.UUID;

@Data
@Builder(builderMethodName = "Builder")
@EqualsAndHashCode(callSuper = true)
@Table("transaction")
public class TransactionEntity extends BaseEntity {

    private Date date;

    private UUID payerAccountId;

    private UUID payeeAccountId;

    private long amount;

    private TransactionType type;

    private TransactionState state;

    private int code;

}

