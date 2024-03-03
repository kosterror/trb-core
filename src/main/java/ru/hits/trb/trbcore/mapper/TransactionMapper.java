package ru.hits.trb.trbcore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.hits.trb.trbcore.dto.transaction.TransactionDto;
import ru.hits.trb.trbcore.entity.TransactionEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TransactionMapper {

    TransactionDto entityToDto(TransactionEntity entity);

}
