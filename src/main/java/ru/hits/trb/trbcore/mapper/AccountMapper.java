package ru.hits.trb.trbcore.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.hits.trb.trbcore.dto.account.AccountDto;
import ru.hits.trb.trbcore.dto.account.NewAccountDto;
import ru.hits.trb.trbcore.entity.AccountEntity;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AccountMapper {

    @Mapping(target = "balance", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "creationDate", expression = "java(new java.util.Date())")
    @Mapping(target = "isClosed", expression = "java(false)")
    AccountEntity newDtoToEntity(NewAccountDto dto);

    AccountDto entityToDto(AccountEntity accountEntity);

}
