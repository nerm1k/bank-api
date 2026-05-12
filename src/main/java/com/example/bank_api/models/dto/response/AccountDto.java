package com.example.bank_api.models.dto.response;

import java.math.BigDecimal;

public record AccountDto(
        Long id,
        BigDecimal balance,
        String pin,
        Long beneficiaryId
//        String beneficiaryName
) {
}
