package com.example.bank_api.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание счета")
public class CreateAccountRequestDto {

        @NotNull(message = "Пин код не может быть пустым.")
        @Pattern(regexp = "[0-9]{4}", message = "Пин код должен состоять из 4-х цифр.")
        @Schema(description = "Пинкод счета", requiredMode = Schema.RequiredMode.REQUIRED)
        private String pin;

        @NotNull(message = "beneficiaryId не может быть пустым.")
        @Schema(description = "Идентификатор клиента", requiredMode = Schema.RequiredMode.REQUIRED)
        private Long beneficiaryId;
}
