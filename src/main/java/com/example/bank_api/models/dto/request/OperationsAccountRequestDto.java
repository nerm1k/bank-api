package com.example.bank_api.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Запрос на совершение транзакции по счету")
public class OperationsAccountRequestDto {

        @NotNull(message = "Пин код не может быть пустым.")
        @Pattern(regexp = "[0-9]{4}", message = "Пин код должен состоять из 4-х цифр.")
        @Schema(description = "Пинкод счета", requiredMode = Schema.RequiredMode.REQUIRED)
        String pin;

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Значение должно быть больше 0.")
        @Digits(integer = 19, fraction = 2, message = "Максимум 2 цифры после запятой.")
        @Schema(description = "Количество средств", requiredMode = Schema.RequiredMode.REQUIRED)
        BigDecimal amount;
}
