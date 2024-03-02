package ru.hits.trb.trbcore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;

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

    private long balance;

    private String clientFullName;

    private UUID externalClientId;

    private Date creationDate;

    private Date closingDate;

    private boolean isClosed;

}
