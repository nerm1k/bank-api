package com.example.bank_api.models.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDto{

    @NotNull
    private Long id;

    @NotNull
    private BigDecimal balance;

    @NotNull
    private String pin;

    @NotNull
    private Long beneficiaryId;
}
