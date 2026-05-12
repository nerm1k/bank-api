package com.example.bank_api;

import com.example.bank_api.models.dto.response.TransactionDto;
import com.example.bank_api.models.entity.AccountEntity;
import com.example.bank_api.models.entity.BeneficiaryEntity;
import com.example.bank_api.models.entity.TransactionEntity;
import com.example.bank_api.models.entity.TransactionType;
import com.example.bank_api.models.mappers.TransactionMapper;
import com.example.bank_api.repositories.TransactionRepository;
import com.example.bank_api.services.TransactionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class TransactionServiceTests {
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    TransactionMapper transactionMapper;
    @InjectMocks
    TransactionService transactionService;

    private static TransactionEntity mockTransactionEntity1;
    private static TransactionEntity mockTransactionEntity2;
    private static TransactionEntity mockTransactionEntity3;
    private static TransactionDto mockTransactionDto1;
    private static TransactionDto mockTransactionDto2;
    private static TransactionDto mockTransactionDto3;

    @BeforeAll
    public static void setup(){
        Long transactionId1 = 1L;
        Long accountFromId1 = 22L;
        Long accountToId1 = 33L;
        BigDecimal balanceChange1 = BigDecimal.valueOf(100.1).negate();
        TransactionType transactionType1 = TransactionType.TRANSFER;
        LocalDateTime createdAt1 = LocalDateTime.of(2026, 5, 8, 14, 39, 0);
        Long transactionId2 = 2L;
        Long accountFromId2 = 5L;
        BigDecimal balanceChange2 = BigDecimal.valueOf(1000);
        TransactionType transactionType2 = TransactionType.DEPOSIT;
        LocalDateTime createdAt2 = LocalDateTime.of(2026, 5, 8, 14, 40, 0);
        Long transactionId3 = 3L;

        AccountEntity mockAccountEntity1 = new AccountEntity(accountFromId1, BigDecimal.valueOf(100), "7777", new BeneficiaryEntity(1L, "Тест1"));
        AccountEntity mockAccountEntity2 = new AccountEntity(accountToId1, BigDecimal.valueOf(200), "5555", new BeneficiaryEntity(2L, "Тест2"));
        AccountEntity mockAccountEntity3 = new AccountEntity(accountFromId2, BigDecimal.valueOf(300), "6666", new BeneficiaryEntity(3L, "Тест3"));

        mockTransactionEntity1 = new TransactionEntity(transactionId1, mockAccountEntity1, mockAccountEntity2, balanceChange1, transactionType1, createdAt1);
        mockTransactionEntity2 = new TransactionEntity(transactionId2, mockAccountEntity3, null, balanceChange2, transactionType2, createdAt2);
        mockTransactionEntity3 = new TransactionEntity(transactionId3, mockAccountEntity1, null, balanceChange2, transactionType2, createdAt2);

        mockTransactionDto1 = new TransactionDto(transactionId1, mockAccountEntity1.getId(), mockAccountEntity2.getId(), balanceChange1, transactionType1, createdAt1);
        mockTransactionDto2 = new TransactionDto(transactionId2, mockAccountEntity3.getId(), null, balanceChange2, transactionType2, createdAt2);
        mockTransactionDto3 = new TransactionDto(transactionId3, mockAccountEntity1.getId(), null, balanceChange2, transactionType2, createdAt2);
    }

    @Test
    public void whenGetAllTransactions_thenAllTransactionsReturned(){
        when(transactionRepository.findAll()).thenReturn(List.of(mockTransactionEntity1, mockTransactionEntity2));
        when(transactionMapper.entityToDto(mockTransactionEntity1)).thenReturn(mockTransactionDto1);
        when(transactionMapper.entityToDto(mockTransactionEntity2)).thenReturn(mockTransactionDto2);

        List<TransactionDto> result = transactionService.findAllTransactions();

        assertNotNull(result);
        assertEquals(List.of(mockTransactionDto1, mockTransactionDto2), result);
    }

    @Test
    public void whenGetAllTransactionsByAccountId_thenAllTransactionsByAccountIdReturned(){
        when(transactionRepository.findAllTransactionsByAccountFromId(mockTransactionEntity1.getAccountFrom().getId()))
                .thenReturn(List.of(mockTransactionEntity1, mockTransactionEntity3));
        when(transactionMapper.entityToDto(mockTransactionEntity1)).thenReturn(mockTransactionDto1);
        when(transactionMapper.entityToDto(mockTransactionEntity3)).thenReturn(mockTransactionDto3);

        List<TransactionDto> result = transactionService.findAllTransactionsByAccountId(mockTransactionEntity1.getAccountFrom().getId());

        assertNotNull(result);
        assertEquals(List.of(mockTransactionDto1, mockTransactionDto3), result);
    }
}
