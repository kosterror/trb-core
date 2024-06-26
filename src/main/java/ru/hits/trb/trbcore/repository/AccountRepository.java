package ru.hits.trb.trbcore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.trb.trbcore.entity.AccountEntity;
import ru.hits.trb.trbcore.entity.enumeration.AccountType;
import ru.hits.trb.trbcore.entity.enumeration.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByIdAndIsClosed(UUID id, boolean isClosed);

    List<AccountEntity> findAllByExternalClientIdAndIsClosedOrderByCreationDate(UUID externalClientId,
                                                                                boolean isClosed
    );

    Optional<AccountEntity> findFirstByBalanceIsGreaterThanEqualAndCurrencyAndType(BigDecimal amount,
                                                                                   Currency currency,
                                                                                   AccountType type
    );

}
