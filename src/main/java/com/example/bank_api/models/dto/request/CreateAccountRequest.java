package com.example.bank_api.models.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;

@Data
public class CreateAccountRequest{

        @NotNull(message = "Пин код не может быть пустым.")
        @Pattern(regexp = "[0-9]{4}", message = "Пин код должен состоять из 4-х цифр.")
        private String pin;

        @NotNull(message = "beneficiaryId не может быть пустым.")
        private Long beneficiaryId;
}
