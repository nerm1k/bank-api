package com.example.bank_api.models.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Данные о счете")
public class AccountDto{

    @NotNull
    @Schema(description = "Идентификатор счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull
    @Schema(description = "Текущий баланс счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal balance;

    @NotNull
    @Schema(description = "Пинкод счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private String pin;

    @NotNull
    @Schema(description = "Идентификатор владельца счета", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long beneficiaryId;
}
