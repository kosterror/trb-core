package ru.hits.trb.trbcore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbcore.entity.enumeration.TransactionType;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID externalId;

    private Date date;

    private UUID payerAccountId;

    private UUID payeeAccountId;

    private long amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

}

