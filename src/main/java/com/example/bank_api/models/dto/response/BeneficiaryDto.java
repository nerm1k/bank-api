package com.example.bank_api.models.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class BeneficiaryDto{

    @NotNull
    private Long id;

    @NotNull
    private String name;
}
