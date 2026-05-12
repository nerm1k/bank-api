package com.example.bank_api.repositories;

import com.example.bank_api.models.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    List<AccountEntity> findAllAccountsByBeneficiaryId(Long beneficiaryId);
}
