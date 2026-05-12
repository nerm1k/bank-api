package com.example.bank_api.models.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

@Data
@Accessors(chain = true)
public class ErrorResponseDto{

    @NotNull
    private HttpStatus httpStatus;

    @NotNull
    private String message;
}
