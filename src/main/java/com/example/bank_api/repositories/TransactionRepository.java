package com.example.bank_api.repositories;

import com.example.bank_api.models.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllTransactionsByAccountFromId(Long accountId);
}
