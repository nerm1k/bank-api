package com.example.bank_api;

import com.example.bank_api.models.dto.request.CreateBeneficiaryRequestDto;
import com.example.bank_api.models.dto.response.AccountDto;
import com.example.bank_api.models.dto.response.BeneficiaryDto;
import com.example.bank_api.models.entity.AccountEntity;
import com.example.bank_api.models.entity.BeneficiaryEntity;
import com.example.bank_api.models.mappers.AccountMapper;
import com.example.bank_api.models.mappers.BeneficiaryMapper;
import com.example.bank_api.repositories.AccountRepository;
import com.example.bank_api.repositories.BeneficiaryRepository;
import com.example.bank_api.services.BeneficiaryService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class BeneficiaryServiceTests {

    @Mock
    BeneficiaryRepository beneficiaryRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    BeneficiaryMapper beneficiaryMapper;
    @Mock
    AccountMapper accountMapper;

    @InjectMocks
    BeneficiaryService beneficiaryService;

    static BeneficiaryEntity mockBeneficiaryEntity1;
    static BeneficiaryEntity mockBeneficiaryEntity2;
    private static AccountEntity mockAccountEntity1;
    private static AccountEntity mockAccountEntity2;
    static BeneficiaryDto mockBeneficiaryDto1;
    static BeneficiaryDto mockBeneficiaryDto2;
    private static AccountDto mockAccountDto1;
    private static AccountDto mockAccountDto2;
    static CreateBeneficiaryRequestDto mockCreateBeneficiaryRequestDto;

    private static Long notExistingBeneficiaryId;


    @BeforeAll
    public static void setup(){
        Long beneficiaryId1 = 1L;
        String beneficiaryName1 = "Тестпервый";
        Long beneficiaryId2 = 2L;
        String beneficiaryName2 = "Тествторой";

        mockBeneficiaryDto1 = new BeneficiaryDto();
        mockBeneficiaryDto1.setId(beneficiaryId1);
        mockBeneficiaryDto1.setName(beneficiaryName1);

        mockBeneficiaryDto2 = new BeneficiaryDto();
        mockBeneficiaryDto2.setId(beneficiaryId2);
        mockBeneficiaryDto2.setName(beneficiaryName2);

        mockCreateBeneficiaryRequestDto = new CreateBeneficiaryRequestDto();
        mockCreateBeneficiaryRequestDto.setName(beneficiaryName1);

        Long accountId1 = 1L;
        Long accountId2 = 2L;
        BigDecimal balance1 = BigDecimal.valueOf(100);
        BigDecimal balance2 = BigDecimal.valueOf(200);
        String pin1 = "7777";
        String pin2 = "5555";

        mockBeneficiaryEntity1 = new BeneficiaryEntity(beneficiaryId1, beneficiaryName1, null);
        mockBeneficiaryEntity2 = new BeneficiaryEntity(beneficiaryId2, beneficiaryName2, null);

        mockAccountEntity1 = new AccountEntity(accountId1, balance1, pin1, mockBeneficiaryEntity1);
        mockAccountEntity2 = new AccountEntity(accountId2, balance2, pin2, mockBeneficiaryEntity1);

        mockBeneficiaryEntity1.setAccounts(List.of(mockAccountEntity1, mockAccountEntity2));
        mockBeneficiaryEntity2.setAccounts(List.of());

        mockBeneficiaryEntity1 = new BeneficiaryEntity(beneficiaryId1, beneficiaryName1, List.of(mockAccountEntity1, mockAccountEntity2));
        mockBeneficiaryEntity2 = new BeneficiaryEntity(beneficiaryId2, beneficiaryName2, List.of());

        mockAccountDto1 = new AccountDto();
        mockAccountDto1.setId(accountId1);
        mockAccountDto1.setBalance(balance1);
        mockAccountDto1.setPin(pin1);

        mockAccountDto1.setBeneficiaryId(mockBeneficiaryEntity1.getId());
        mockAccountDto2 = new AccountDto();
        mockAccountDto2.setId(accountId2);
        mockAccountDto2.setBalance(balance2);
        mockAccountDto2.setPin(pin2);
        mockAccountDto2.setBeneficiaryId(mockBeneficiaryEntity1.getId());

        notExistingBeneficiaryId = 4L;
    }


    @Test
    public void whenGetAllBeneficiaries_thenAllBeneficiariesReturned() {
        when(beneficiaryRepository.findAll()).thenReturn(List.of(mockBeneficiaryEntity1, mockBeneficiaryEntity2));
        when(beneficiaryMapper.entityToDto(mockBeneficiaryEntity1)).thenReturn(mockBeneficiaryDto1);
        when(beneficiaryMapper.entityToDto(mockBeneficiaryEntity2)).thenReturn(mockBeneficiaryDto2);

        List<BeneficiaryDto> result = beneficiaryService.findAllBeneficiaries();

        assertNotNull(result);
        assertEquals(List.of(mockBeneficiaryDto1, mockBeneficiaryDto2), result);
    }

    @Test
    public void whenCreateBeneficiary_thenNewBeneficiaryReturned(){
        when(beneficiaryMapper.requestDtoToEntity(mockCreateBeneficiaryRequestDto)).thenReturn(mockBeneficiaryEntity1);
        when(beneficiaryRepository.save(mockBeneficiaryEntity1)).thenReturn(mockBeneficiaryEntity1);
        when(beneficiaryMapper.entityToDto(mockBeneficiaryEntity1)).thenReturn(mockBeneficiaryDto1);

        BeneficiaryDto result = beneficiaryService.createBeneficiary(mockCreateBeneficiaryRequestDto);

        assertNotNull(result);
        assertEquals(mockBeneficiaryDto1, result);
    }

    @Test
    public void whenGetAllAccountsByBeneficiaryId_thenAllAccountsByBeneficiaryIdReturned(){
        when(beneficiaryRepository.findById(mockBeneficiaryEntity1.getId())).thenReturn(Optional.of(mockBeneficiaryEntity1));
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(mockAccountDto1);
        when(accountMapper.entityToDto(mockAccountEntity2)).thenReturn(mockAccountDto2);

        List<AccountDto> result = beneficiaryService.findAllAccountsByBeneficiaryId(mockBeneficiaryEntity1.getId());

        assertNotNull(result);
        assertEquals(List.of(mockAccountDto1, mockAccountDto2), result);
    }

    @Test
    public void whenGetAllAccountsByNotExistingBeneficiaryId_thenThrowEntityNotFoundExceptionReturned(){
        when(beneficiaryRepository.findById(notExistingBeneficiaryId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> beneficiaryService.findAllAccountsByBeneficiaryId(notExistingBeneficiaryId));

        assertEquals("Клиента с id " + notExistingBeneficiaryId + " нет.", thrown.getMessage());
    }
}
