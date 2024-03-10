package ru.hits.trb.trbcore.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hits.trb.trbcore.entity.TransactionEntity;

import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    Page<TransactionEntity> findAllByPayeeAccountIdOrPayerAccountId(Pageable pageable,
                                                                    UUID payerAccountId,
                                                                    UUID payeeAccountId);

}
