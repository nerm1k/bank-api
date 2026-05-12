package com.example.bank_api.models.mappers;

import com.example.bank_api.models.dto.response.TransactionDto;
import com.example.bank_api.models.entity.TransactionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
//    @Mapping(source = "id", target = "id") иногда надо, иногда нет
    @Mapping(source = "transactionType", target = "type")
    @Mapping(source = "accountFrom.id", target = "accountFromId")
    @Mapping(source = "accountTo.id", target = "accountToId")
    TransactionDto entityToDto(TransactionEntity transactionEntity);
}
