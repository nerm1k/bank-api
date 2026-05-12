package com.example.bank_api.models.dto.response;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(
    HttpStatus httpStatus,
    String message
) {
}
