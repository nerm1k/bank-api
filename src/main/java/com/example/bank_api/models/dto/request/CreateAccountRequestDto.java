package com.example.bank_api.models.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAccountRequestDto {

        @NotNull(message = "Пин код не может быть пустым.")
        @Pattern(regexp = "[0-9]{4}", message = "Пин код должен состоять из 4-х цифр.")
        private String pin;

        @NotNull(message = "beneficiaryId не может быть пустым.")
        private Long beneficiaryId;
}
