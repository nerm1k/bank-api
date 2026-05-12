package com.example.bank_api.controllers;

import com.example.bank_api.models.dto.response.TransactionDto;
import com.example.bank_api.services.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    @Operation(summary = "Получение всех транзакций",
            description = "В ответе возвращается список объектов Transaction с полями transactionId, accountFromId, accountToId, balanceChange, type, createdAt")
    @ApiResponse(responseCode = "200", description = "Транзакции успешно найдены.")
    public ResponseEntity<List<TransactionDto>> getAllTransactions(){
        return ResponseEntity.status(200).body(transactionService.findAllTransactions());
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Получение всех транзакций по счету",
            description = "В ответе возвращается список объектов Transaction с полями transactionId, accountFromId, accountToId, balanceChange, type, createdAt")
    @ApiResponse(responseCode = "200", description = "Транзакции по счету успешно найдены.")
    public ResponseEntity<List<TransactionDto>> getAllTransactionsByAccountId(
            @PathVariable(value = "accountId") Long accountId
    ){
        return ResponseEntity.status(200).body(transactionService.findAllTransactionsByAccountId(accountId));
    }
}
