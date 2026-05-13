package com.example.bank_api.controllers;

import com.example.bank_api.models.dto.request.CreateAccountRequestDto;
import com.example.bank_api.models.dto.request.OperationsAccountRequestDto;
import com.example.bank_api.models.dto.response.AccountDto;
import com.example.bank_api.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping
    @Operation(summary = "Получить все счета клиентов",
            description = "В ответе возвращается список объектов Account c полями id, balance, pin, beneficiaryId.")
    @ApiResponse(responseCode = "200", description = "Счета успешно найдены.")
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        return ResponseEntity.status(200).body(accountService.findAllAccounts());
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Получить счет по id",
            description = "В ответе возвращается объект Account c полями id, balance, pin, beneficiaryId.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счет успешно найден."),
            @ApiResponse(responseCode = "404", description = "Данный счет отсутствует.")
    })
    public ResponseEntity<AccountDto> getAccountById(
            @PathVariable(value = "accountId") Long accountId
    ){
        return ResponseEntity.ok(accountService.findAccountById(accountId));
    }

    @PostMapping
    @Operation(summary = "Создать счет",
            description = "В ответе возвращается объект Account c полями id, balance, pin, beneficiaryId.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Счет успешно создан."),
            @ApiResponse(responseCode = "400", description = "Задан некорректный пинкод."),
            @ApiResponse(responseCode = "404", description = "Данный клиент отсутствует.")
    })
    public ResponseEntity<AccountDto> createAccount(
            @Valid @RequestBody CreateAccountRequestDto accountToCreate
    ){
        return ResponseEntity.status(201).body(accountService.createAccount(accountToCreate));
    }

    @PostMapping("/{accountId}/deposit")
    @Operation(summary = "Пополнить счет",
            description = "В ответе возвращается объект Account c полями id, balance, pin, beneficiaryId.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Счет успешно пополнен."),
            @ApiResponse(responseCode = "400", description = "Некорректная сумма."),
            @ApiResponse(responseCode = "403", description = "Неверный pin. Доступ отказан."),
            @ApiResponse(responseCode = "404", description = "Клиент не найден или счет не принадлежит клиенту.")
    })
    public ResponseEntity<AccountDto> depositAccount(
            @PathVariable(value = "accountId") Long accountId,
            @Valid @RequestBody OperationsAccountRequestDto depositAccountRequest
    ){
        return ResponseEntity.ok(accountService.depositAccount(accountId, depositAccountRequest));  //хоть это и post, но новый ресурс (счет) не создается, поэтому ответ 200.
        //  Если бы финансовые операции были реализованы в TransactionalController, тогда 201 был бы логичен.
    }

    @PostMapping("/{accountId}/withdraw")
    @Operation(summary = "Вывести со счета",
            description = "В ответе возвращается объект Account c полями id, balance, pin, beneficiaryId.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Вывод успешно прошел со счета."),
            @ApiResponse(responseCode = "400", description = "Некорректная или недостуточная сумма."),
            @ApiResponse(responseCode = "403", description = "Неверный pin. Доступ отказан."),
            @ApiResponse(responseCode = "404", description = "Клиент не найден или счет не принадлежит клиенту.")
    })
    public ResponseEntity<AccountDto> withdrawFromAccount(
            @PathVariable(value = "accountId") Long accountId,
            @Valid @RequestBody OperationsAccountRequestDto withdrawAccountRequest
    ){
        return ResponseEntity.ok(accountService.withdrawFromAccount(accountId, withdrawAccountRequest));
    }

    @PostMapping("/{accountFromId}/transfer/{accountToId}")
    @Operation(summary = "Перевод с одного счета на другой.",
            description = "В ответе возвращается два объекта (отправителя и получателя) Account c полями id, balance, pin, beneficiaryId.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Перевод успешно проведен."),
            @ApiResponse(responseCode = "400", description = "Некорректная или недостуточная сумма."),
            @ApiResponse(responseCode = "403", description = "Неверный pin. Доступ отказан."),
            @ApiResponse(responseCode = "404", description = "Клиент не найден или счет не принадлежит клиенту.")
    })
    public ResponseEntity<List<AccountDto>> transferFromAccountToAccount(
        @PathVariable(name = "accountFromId") Long accountFromId,
        @PathVariable(name = "accountToId") Long accountToId,
        @Valid @RequestBody OperationsAccountRequestDto transferAccountRequest
    ){
        return ResponseEntity.ok(accountService.transferFromAccountToAccount(accountFromId, accountToId, transferAccountRequest));
    }

}
