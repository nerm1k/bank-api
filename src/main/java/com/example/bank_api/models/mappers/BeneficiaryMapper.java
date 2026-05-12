package com.example.bank_api.models.mappers;

import com.example.bank_api.models.dto.request.CreateBeneficiaryRequestDto;
import com.example.bank_api.models.dto.response.BeneficiaryDto;
import com.example.bank_api.models.entity.BeneficiaryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BeneficiaryMapper {
    BeneficiaryEntity requestDtoToEntity(CreateBeneficiaryRequestDto beneficiary);

    BeneficiaryDto entityToDto(BeneficiaryEntity beneficiaryEntity);
}
