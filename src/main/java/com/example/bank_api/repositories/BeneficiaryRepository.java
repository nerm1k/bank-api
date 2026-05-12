package com.example.bank_api.repositories;

import com.example.bank_api.models.entity.BeneficiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiaryRepository extends JpaRepository<BeneficiaryEntity, Long> {
}
