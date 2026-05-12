package com.example.bank_api.controllers;

import com.example.bank_api.models.dto.request.CreateBeneficiaryRequestDto;
import com.example.bank_api.models.dto.response.AccountDto;
import com.example.bank_api.models.dto.response.BeneficiaryDto;
import com.example.bank_api.services.BeneficiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService beneficiaryService;

    @GetMapping
    @Operation(summary = "Получить всех клиентов", description = "В ответе возвращается список объектов Beneficiary c полями id и name.")
    @ApiResponse(responseCode = "200", description = "Клиенты найдены.")
    public ResponseEntity<List<BeneficiaryDto>> getAllBeneficiaries(){
        return ResponseEntity.ok().body(beneficiaryService.findAllBeneficiaries());
    }

    @PostMapping
    @Operation(summary = "Создать клиента", description = "В ответе возвращается объект Beneficiary c полями id и name.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Клиент успешно создан."),
            @ApiResponse(responseCode = "400", description = "Клиент не создан, ошибка в запросе.")
    })
    public ResponseEntity<BeneficiaryDto> createBeneficiary(
            @Valid @RequestBody CreateBeneficiaryRequestDto beneficiaryToCreate
    ){
        return ResponseEntity.status(201).body(beneficiaryService.createBeneficiary(beneficiaryToCreate));
    }

    @GetMapping("/{beneficiaryId}/accounts")
    @Operation(summary = "Получить все счета клиента по id клиента",
            description = "В ответе возвращается список объектов Account c полями id, balance, pin, beneficiaryId.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Счета успешно найдены."),
            @ApiResponse(responseCode = "404", description = "У данного клиента отсутствуют счета.")
    })
    public ResponseEntity<List<AccountDto>> getAllAccountsByBeneficiaryId(
            @PathVariable(value = "beneficiaryId") Long beneficiaryId
    ){
        return ResponseEntity.status(200).body(beneficiaryService.findAllAccountsByBeneficiaryId(beneficiaryId));
    }
}
