package com.example.bank_api.models.dto.response;

import com.example.bank_api.models.entity.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionDto(
        Long id,
        Long accountFromId,
        Long accountToId,
        BigDecimal balanceChange,
        TransactionType type,
        LocalDateTime createdAt
) {
}
