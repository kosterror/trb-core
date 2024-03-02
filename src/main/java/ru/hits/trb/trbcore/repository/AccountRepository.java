package ru.hits.trb.trbcore.repository;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import ru.hits.trb.trbcore.entity.AccountEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends ListCrudRepository<AccountEntity, UUID> {

    Optional<AccountEntity> findByIdAndIsClosed(UUID id, boolean isClosedd);

}
