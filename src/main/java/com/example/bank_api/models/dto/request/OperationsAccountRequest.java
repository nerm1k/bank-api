package com.example.bank_api.models.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record OperationsAccountRequest(
        @NotNull(message = "Пин код не может быть пустым.")
        @Pattern(regexp = "[0-9]{4}", message = "Пин код должен состоять из 4-х цифр.")
        String pin,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Значение должно быть больше 0.")
        @Digits(integer = 19, fraction = 2, message = "Максимум 2 цифры после запятой.")
        BigDecimal amount
) {
}
