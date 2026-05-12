package com.example.bank_api.services;

import com.example.bank_api.models.dto.request.CreateBeneficiaryRequestDto;
import com.example.bank_api.models.dto.response.AccountDto;
import com.example.bank_api.models.dto.response.BeneficiaryDto;
import com.example.bank_api.models.entity.AccountEntity;
import com.example.bank_api.models.entity.BeneficiaryEntity;
import com.example.bank_api.models.mappers.AccountMapper;
import com.example.bank_api.models.mappers.BeneficiaryMapper;
import com.example.bank_api.repositories.BeneficiaryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {
    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryMapper beneficiaryMapper;
    private final AccountMapper accountMapper;

    public List<BeneficiaryDto> findAllBeneficiaries() {
        List<BeneficiaryEntity> beneficiaryEntityList = beneficiaryRepository.findAll();

        return beneficiaryEntityList.stream()
                .map(beneficiaryMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public BeneficiaryDto createBeneficiary(CreateBeneficiaryRequestDto beneficiaryToCreate) {
        BeneficiaryEntity beneficiaryEntity = beneficiaryMapper.requestDtoToEntity(beneficiaryToCreate);
        BeneficiaryEntity savedBeneficiaryEntity = this.beneficiaryRepository.save(beneficiaryEntity);

        return beneficiaryMapper.entityToDto(savedBeneficiaryEntity);
    }

    public List<AccountDto> findAllAccountsByBeneficiaryId(Long beneficiaryId) {
        BeneficiaryEntity beneficiaryEntity = this.beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new EntityNotFoundException("Клиента с id " + beneficiaryId + " нет."));

        List<AccountEntity>  accountEntityList = beneficiaryEntity.getAccounts();

        return accountEntityList.stream()
                .sorted(Comparator.comparing(AccountEntity::getId))
                .map(accountMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
