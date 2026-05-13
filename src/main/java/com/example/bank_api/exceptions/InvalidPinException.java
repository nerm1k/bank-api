package com.example.bank_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) //у тебя FORBIDDEN в ExceptionHandler .
// И тут 1 из 2. Или ставить эту аннотацию, тогда обработчик в GlobalExceptionsHandler не нужен, но в ответе с ошибкой не будет message. Или обрабатывать в GlobalExceptionsHandler, тогда @ResponseStatus не нужен.
public class InvalidPinException extends RuntimeException {
    public InvalidPinException(String message){
        super(message);
    }
}
