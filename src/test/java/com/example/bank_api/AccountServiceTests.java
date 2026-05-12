package com.example.bank_api;

import com.example.bank_api.exceptions.InvalidPinException;
import com.example.bank_api.models.dto.request.CreateAccountRequestDto;
import com.example.bank_api.models.dto.request.OperationsAccountRequestDto;
import com.example.bank_api.models.dto.response.AccountDto;
import com.example.bank_api.models.entity.AccountEntity;
import com.example.bank_api.models.entity.BeneficiaryEntity;
import com.example.bank_api.models.mappers.AccountMapper;
import com.example.bank_api.repositories.AccountRepository;
import com.example.bank_api.repositories.BeneficiaryRepository;
import com.example.bank_api.repositories.TransactionRepository;
import com.example.bank_api.services.AccountService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class AccountServiceTests {
    @Mock
    AccountRepository accountRepository;
    @Mock
    TransactionRepository transactionRepository;
    @Mock
    BeneficiaryRepository beneficiaryRepository;
    @Mock
    AccountMapper accountMapper;
    @InjectMocks
    AccountService accountService;

    private static AccountEntity mockAccountEntity1;
    private static AccountEntity mockAccountEntity2;
    private static AccountEntity mockAccountEntity3;
    private static AccountDto mockAccountDto1;
    private static AccountDto mockAccountDto2;
    private static AccountDto mockAccountDto3;
    private static BeneficiaryEntity mockBeneficiaryEntity1;
    private static BeneficiaryEntity mockBeneficiaryEntity2;
    private static CreateAccountRequestDto mockCreateAccountRequestDto;
    private static OperationsAccountRequestDto mockOperationsAccountRequestDto;
    private static Long notExistingBeneficiaryId;
    private static Long notExistingAccountId;


    @BeforeAll
    public static void setup(){
        Long beneficiaryId1 = 1L;
        String beneficiaryName1 = "Тестпервый";
        Long beneficiaryId2 = 2L;
        String beneficiaryName2 = "Тествторой";

        Long accountId1 = 1L;
        Long accountId2 = 2L;
        Long accountId3 = 3L;
        BigDecimal balance1 = BigDecimal.valueOf(100);
        BigDecimal balance2 = BigDecimal.valueOf(200);
        BigDecimal balance3 = BigDecimal.valueOf(300);
        String pin1 = "7777";
        String pin2 = "5555";
        String pin3 = "6666";

        mockBeneficiaryEntity1 = new BeneficiaryEntity(beneficiaryId1, beneficiaryName1, null);
        mockBeneficiaryEntity2 = new BeneficiaryEntity(beneficiaryId2, beneficiaryName2, null);

        mockAccountEntity1 = new AccountEntity(accountId1, balance1, pin1, mockBeneficiaryEntity1);
        mockAccountEntity2 = new AccountEntity(accountId2, balance2, pin2, mockBeneficiaryEntity1);
        mockAccountEntity3 = new AccountEntity(accountId3, balance3, pin3, mockBeneficiaryEntity2);

        mockBeneficiaryEntity1.setAccounts(List.of(mockAccountEntity1, mockAccountEntity2));
        mockBeneficiaryEntity2.setAccounts(List.of(mockAccountEntity3));

        mockAccountDto1 = new AccountDto();
        mockAccountDto1.setId(accountId1);
        mockAccountDto1.setBalance(balance1);
        mockAccountDto1.setPin(pin1);
        mockAccountDto1.setBeneficiaryId(mockAccountEntity1.getId());

        mockAccountDto2 = new AccountDto();
        mockAccountDto2.setId(accountId2);
        mockAccountDto2.setBalance(balance2);
        mockAccountDto2.setPin(pin2);
        mockAccountDto2.setBeneficiaryId(mockAccountEntity1.getId());

        mockAccountDto3 = new AccountDto();
        mockAccountDto1.setId(accountId3);
        mockAccountDto1.setBalance(balance3);
        mockAccountDto1.setPin(pin3);
        mockAccountDto1.setBeneficiaryId(mockAccountEntity2.getId());

        mockCreateAccountRequestDto = new CreateAccountRequestDto();
        mockCreateAccountRequestDto.setPin("3333");
        mockCreateAccountRequestDto.setBeneficiaryId(beneficiaryId2);

        mockOperationsAccountRequestDto = new OperationsAccountRequestDto();
        mockOperationsAccountRequestDto.setPin(mockAccountEntity1.getPin());
        mockOperationsAccountRequestDto.setAmount(BigDecimal.valueOf(15));

        notExistingBeneficiaryId = 4L;
        notExistingAccountId = 50L;
    }

    @Test
    public void whenGetAllAccounts_thenAllAccountsReturned(){
        when(accountRepository.findAll()).thenReturn(List.of(mockAccountEntity1, mockAccountEntity2, mockAccountEntity3));
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(mockAccountDto1);
        when(accountMapper.entityToDto(mockAccountEntity2)).thenReturn(mockAccountDto2);
        when(accountMapper.entityToDto(mockAccountEntity3)).thenReturn(mockAccountDto3);

        List<AccountDto> result = accountService.findAllAccounts();

        assertNotNull(result);
        assertEquals(List.of(mockAccountDto1, mockAccountDto2, mockAccountDto3), result);
    }

    @Test
    public void whenGetAccountById_thenAccountByIdReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(mockAccountDto1);

        AccountDto result = accountService.findAccountById(mockAccountEntity1.getId());

        assertNotNull(result);
        assertEquals(mockAccountDto1, result);
    }

    @Test
    public void whenGetNotExistingAccountById_thenThrowEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.findAccountById(notExistingAccountId));

        assertEquals("Счета с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenCreateNewAccount_thenNewAccountReturned(){
        Long newAccountId = 4L;

        AccountEntity mappedAccountEntity = new AccountEntity();
        mappedAccountEntity.setPin(mockCreateAccountRequestDto.getPin());
        mappedAccountEntity.setBalance(BigDecimal.valueOf(0));

        AccountEntity savedAccountEntity = new AccountEntity(
                newAccountId,
                BigDecimal.valueOf(0),
                mockCreateAccountRequestDto.getPin(),
                mockBeneficiaryEntity2
        );

        AccountDto newAccountDto = new AccountDto();
        newAccountDto.setId(newAccountId);
        newAccountDto.setBalance(BigDecimal.valueOf(0));
        newAccountDto.setPin(mockCreateAccountRequestDto.getPin());
        newAccountDto.setBeneficiaryId(mockBeneficiaryEntity2.getId());

        when(beneficiaryRepository.findById(mockCreateAccountRequestDto.getBeneficiaryId())).thenReturn(Optional.of(mockBeneficiaryEntity2));
        when(accountMapper.dtoToEntity(mockCreateAccountRequestDto)).thenReturn(mappedAccountEntity);
        when(accountRepository.save(mappedAccountEntity)).thenReturn(savedAccountEntity);
        when(accountMapper.entityToDto(savedAccountEntity)).thenReturn(newAccountDto);

        AccountDto result = accountService.createAccount(mockCreateAccountRequestDto);

        assertNotNull(result);
        assertEquals(newAccountDto, result);
    }

    @Test
    public void whenCreateNewAccountWithNotExistingBeneficiaryId_thenEntityNotFoundExceptionReturned(){
        when(beneficiaryRepository.findById(notExistingBeneficiaryId)).thenReturn(Optional.empty());

        CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();
        createAccountRequestDto.setPin("3333");
        createAccountRequestDto.setBeneficiaryId(notExistingBeneficiaryId);

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.createAccount(createAccountRequestDto));

        assertEquals("Клиента с id " + notExistingBeneficiaryId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenDepositAccountSuccessfully_thenAccountReturned(){
        mockAccountEntity1.setBalance(mockAccountEntity1.getBalance().add(mockOperationsAccountRequestDto.getAmount()));
        AccountDto savedAccountDto1 = new AccountDto();
        savedAccountDto1.setId(mockAccountEntity1.getId());
        savedAccountDto1.setBalance(mockAccountEntity1.getBalance());
        savedAccountDto1.setPin(mockAccountEntity1.getPin());
        savedAccountDto1.setBeneficiaryId(mockAccountEntity1.getBeneficiary().getId());

        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.save(mockAccountEntity1)).thenReturn(mockAccountEntity1);
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(savedAccountDto1);

        AccountDto result = accountService.depositAccount(mockAccountEntity1.getId(), mockOperationsAccountRequestDto);

        assertNotNull(result);
        assertEquals(savedAccountDto1, result);
    }

    @Test
    public void whenDepositNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.depositAccount(notExistingAccountId, mockOperationsAccountRequestDto));

        assertEquals("Счета с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenDepositAccountWithWrongPin_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));

        OperationsAccountRequestDto operationsAccountRequestDto = new OperationsAccountRequestDto();
        operationsAccountRequestDto.setPin("1244");
        operationsAccountRequestDto.setAmount(BigDecimal.valueOf(111));

        InvalidPinException thrown =  assertThrows(InvalidPinException.class,
                () -> accountService.depositAccount(mockAccountEntity1.getId(), operationsAccountRequestDto));

        assertEquals("Неверный pin. Доступ отказан.", thrown.getMessage());
    }

    @Test
    public void whenWithdrawFromAccountSuccessfully_thenAccountReturned(){
        mockAccountEntity1.setBalance(mockAccountEntity1.getBalance().subtract(mockOperationsAccountRequestDto.getAmount()));
        AccountDto savedAccountDto1 = new AccountDto();
        savedAccountDto1.setId(mockAccountEntity1.getId());
        savedAccountDto1.setBalance(mockAccountEntity1.getBalance());
        savedAccountDto1.setPin(mockAccountEntity1.getPin());
        savedAccountDto1.setBeneficiaryId(mockAccountEntity1.getBeneficiary().getId());

        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.save(mockAccountEntity1)).thenReturn(mockAccountEntity1);
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(savedAccountDto1);

        AccountDto result = accountService.withdrawFromAccount(mockAccountEntity1.getId(), mockOperationsAccountRequestDto);

        assertNotNull(result);
        assertEquals(savedAccountDto1, result);
    }

    @Test
    public void whenWithdrawFromNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.withdrawFromAccount(notExistingAccountId, mockOperationsAccountRequestDto));

        assertEquals("Счета с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenWithdrawFromAccountWithWrongPin_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));

        OperationsAccountRequestDto operationsAccountRequestDto = new OperationsAccountRequestDto();
        operationsAccountRequestDto.setPin("1244");
        operationsAccountRequestDto.setAmount(BigDecimal.valueOf(111));

        InvalidPinException thrown =  assertThrows(InvalidPinException.class,
                () -> accountService.withdrawFromAccount(mockAccountEntity1.getId(), operationsAccountRequestDto));

        assertEquals("Неверный pin. Доступ отказан.", thrown.getMessage());
    }

    @Test
    public void whenWithdrawFromAccountWithBiggerAmount_thenIllegalArgumentExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));

        OperationsAccountRequestDto operationsAccountRequestDto = new OperationsAccountRequestDto();
        operationsAccountRequestDto.setPin(mockAccountEntity1.getPin());
        operationsAccountRequestDto.setAmount(BigDecimal.valueOf(555555L));

        IllegalArgumentException thrown =  assertThrows(IllegalArgumentException.class,
                () -> accountService.withdrawFromAccount(mockAccountEntity1.getId(), operationsAccountRequestDto));

        assertEquals("Недостаточно средств для вывода.", thrown.getMessage());
    }

    @Test
    public void whenDepositFromAccountToAccountSuccessfully_thenAccountsReturned(){
        mockAccountEntity1.setBalance(mockAccountEntity1.getBalance().subtract(mockOperationsAccountRequestDto.getAmount()));
        mockAccountEntity3.setBalance(mockAccountEntity3.getBalance().add(mockOperationsAccountRequestDto.getAmount()));
        AccountDto savedAccountDto1 = new AccountDto();
        savedAccountDto1.setId(mockAccountEntity1.getId());
        savedAccountDto1.setBalance(mockAccountEntity1.getBalance());
        savedAccountDto1.setPin(mockAccountEntity1.getPin());
        savedAccountDto1.setBeneficiaryId(mockAccountEntity1.getBeneficiary().getId());

        AccountDto savedAccountDto2 = new AccountDto();
        savedAccountDto2.setId(mockAccountEntity3.getId());
        savedAccountDto2.setBalance(mockAccountEntity3.getBalance());
        savedAccountDto2.setPin(mockAccountEntity3.getPin());
        savedAccountDto2.setBeneficiaryId(mockAccountEntity3.getBeneficiary().getId());

        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(mockAccountEntity3.getId())).thenReturn(Optional.of(mockAccountEntity3));
        when(accountRepository.save(mockAccountEntity1)).thenReturn(mockAccountEntity1);
        when(accountRepository.save(mockAccountEntity3)).thenReturn(mockAccountEntity3);
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(savedAccountDto1);
        when(accountMapper.entityToDto(mockAccountEntity3)).thenReturn(savedAccountDto2);

        List<AccountDto> result = accountService.transferFromAccountToAccount(mockAccountEntity1.getId(),
                                                                                mockAccountEntity3.getId(),
                mockOperationsAccountRequestDto);

        assertNotNull(result);
        assertEquals(List.of(savedAccountDto1, savedAccountDto2), result);
    }

    @Test
    public void whenTransferFromNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.transferFromAccountToAccount(notExistingAccountId,
                                                                    mockAccountEntity3.getId(),
                        mockOperationsAccountRequestDto));

        assertEquals("Счета отправителя с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenTransferToNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.transferFromAccountToAccount(mockAccountEntity1.getId(),
                                                                    notExistingAccountId,
                        mockOperationsAccountRequestDto));

        assertEquals("Счета получателя с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenTransferFromAccountToAccountWithWrongPin_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(mockAccountEntity3.getId())).thenReturn(Optional.of(mockAccountEntity3));

        OperationsAccountRequestDto operationsAccountRequestDto = new OperationsAccountRequestDto();
        operationsAccountRequestDto.setPin("2221");
        operationsAccountRequestDto.setAmount(BigDecimal.valueOf(2L));

        InvalidPinException thrown =  assertThrows(InvalidPinException.class,
                () -> accountService.transferFromAccountToAccount(mockAccountEntity1.getId(), mockAccountEntity3.getId(), operationsAccountRequestDto));

        assertEquals("Неверный pin. Доступ отказан.", thrown.getMessage());
    }

    @Test
    public void whenTransferFromAccountToAccountWithBiggerAmount_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(mockAccountEntity3.getId())).thenReturn(Optional.of(mockAccountEntity3));

        OperationsAccountRequestDto operationsAccountRequestDto = new OperationsAccountRequestDto();
        operationsAccountRequestDto.setPin(mockAccountEntity1.getPin());
        operationsAccountRequestDto.setAmount(BigDecimal.valueOf(555555L));

        IllegalArgumentException thrown =  assertThrows(IllegalArgumentException.class,
                () -> accountService.transferFromAccountToAccount(mockAccountEntity1.getId(), mockAccountEntity3.getId(), operationsAccountRequestDto));

        assertEquals("Недостаточно средств для перевода.", thrown.getMessage());
    }
}
