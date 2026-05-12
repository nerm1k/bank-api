package com.example.bank_api.models.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Данные о клиенте")
public class BeneficiaryDto{

    @NotNull
    @Schema(description = "Идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull
    @Schema(description = "Имя клиента", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
