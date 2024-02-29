package ru.hits.trb.trbcore.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.relational.core.mapping.Table;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;

import java.util.Date;
import java.util.UUID;

@Data
@Builder(builderMethodName = "Builder")
@EqualsAndHashCode(callSuper = true)
@Table("account")
public class AccountEntity extends BaseEntity {

    private AccountType type;

    private long balance;

    private String clientFullName;

    private UUID externalClientId;

    private Date creationDate;

    private Date closingDate;

    private boolean isClosed;

}
