package com.example.bank_api.models.dto.response;

import com.example.bank_api.models.entity.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Данные о транзакции")
public class TransactionDto{

    @NotNull
    @Schema(description = "Идентификатор транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotNull
    @Schema(description = "Идентификатор счета пополнения или отправителя", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long accountFromId;

    @Schema(description = "Идентификатор счета получателя")
    private Long accountToId;

    @NotNull
    @Schema(description = "Изменение баланса счета пополнения или отправителя", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal balanceChange;

    @NotNull
    @Schema(description = "Тип транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private TransactionType type;

    @NotNull
    @Schema(description = "Дата и время транзакции", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createdAt;
}
