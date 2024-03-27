package ru.hits.trb.trbcore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;
import ru.hits.trb.trbcore.entity.enumeration.Currency;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private UUID loanId;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String clientFullName;

    private UUID externalClientId;

    private Date creationDate;

    private Date closingDate;

    private Boolean isClosed;

}
