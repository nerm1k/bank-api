package com.example.bank_api.models.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на создание клиента")
public class CreateBeneficiaryRequestDto {

        @NotNull(message = "Имя клиента не может быть пустым.")
        @Size(min = 2, max = 32, message = "Имя клиента должно содержать от 2 до 32 символов.")
        @Pattern(regexp = "[А-ЯЁ][а-яё]+", message = "Имя должно соответствовать паттерну [А-Яа-яЁё]+.")
        @Schema(description = "Имя клиента", requiredMode = Schema.RequiredMode.REQUIRED)
        String name;
}
