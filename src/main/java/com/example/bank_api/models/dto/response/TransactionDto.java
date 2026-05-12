package com.example.bank_api.models.dto.response;

import com.example.bank_api.models.entity.TransactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDto{

    @NotNull
    private Long id;

    @NotNull
    private Long accountFromId;

    private Long accountToId;

    @NotNull
    private BigDecimal balanceChange;

    @NotNull
    private TransactionType type;

    @NotNull
    private LocalDateTime createdAt;
}
