package com.example.bank_api.models.mappers;

import com.example.bank_api.models.dto.request.CreateAccountRequest;
import com.example.bank_api.models.dto.response.AccountDto;
import com.example.bank_api.models.entity.AccountEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.stereotype.Component;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

//    @Mapping(source = "id", target = "id")
    @Mapping(source = "beneficiary.id", target = "beneficiaryId")
//    @Mapping(source = "beneficiary.name", target = "beneficiaryName")
    AccountDto entityToDto(AccountEntity entity);

    AccountEntity dtoToEntity(CreateAccountRequest createAccountRequest);
}
