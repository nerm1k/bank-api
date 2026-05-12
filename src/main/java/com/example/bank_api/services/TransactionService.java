package com.example.bank_api.services;

import com.example.bank_api.models.dto.response.TransactionDto;
import com.example.bank_api.models.entity.TransactionEntity;
import com.example.bank_api.models.mappers.TransactionMapper;
import com.example.bank_api.repositories.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    public List<TransactionDto> findAllTransactions() {
        List<TransactionEntity> transactionEntityList = this.transactionRepository.findAll();

        return transactionEntityList.stream()
                .map(transactionMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> findAllTransactionsByAccountId(Long accountId) {
        List<TransactionEntity> transactionEntityList = this.transactionRepository.findAllTransactionsByAccountFromId(accountId);
        return transactionEntityList.stream()
                .map(transactionMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
