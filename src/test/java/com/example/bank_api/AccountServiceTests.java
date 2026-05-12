package com.example.bank_api;

import com.example.bank_api.exceptions.InvalidPinException;
import com.example.bank_api.models.dto.request.CreateAccountRequest;
import com.example.bank_api.models.dto.request.CreateBeneficiaryRequest;
import com.example.bank_api.models.dto.request.OperationsAccountRequest;
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
    private static CreateAccountRequest mockCreateAccountRequest;
    private static OperationsAccountRequest mockOperationsAccountRequest;
    private static Long notExistingBeneficiaryId;
    private static Long notExistingAccountId;


    @BeforeAll
    public static void setup(){
        Long beneficiaryId1 = 1L;
        String beneficiaryName1 = "Тестпервый";
        Long beneficiaryId2 = 2L;
        String beneficiaryName2 = "Тествторой";
        mockBeneficiaryEntity1 = new BeneficiaryEntity(beneficiaryId1, beneficiaryName1);
        mockBeneficiaryEntity2 = new BeneficiaryEntity(beneficiaryId2, beneficiaryName2);

        Long accountId1 = 1L;
        Long accountId2 = 2L;
        Long accountId3 = 3L;
        BigDecimal balance1 = BigDecimal.valueOf(100);
        BigDecimal balance2 = BigDecimal.valueOf(200);
        BigDecimal balance3 = BigDecimal.valueOf(300);
        String pin1 = "7777";
        String pin2 = "5555";
        String pin3 = "6666";

        mockAccountEntity1 = new AccountEntity(accountId1, balance1, pin1, mockBeneficiaryEntity1);
        mockAccountEntity2 = new AccountEntity(accountId2, balance2, pin2, mockBeneficiaryEntity1);
        mockAccountEntity3 = new AccountEntity(accountId3, balance3, pin3, mockBeneficiaryEntity2);

        mockAccountDto1 = new AccountDto(accountId1, balance1, pin1, mockBeneficiaryEntity1.getId());
        mockAccountDto2 = new AccountDto(accountId2, balance2, pin2, mockBeneficiaryEntity1.getId());
        mockAccountDto3 = new AccountDto(accountId3, balance3, pin3, mockBeneficiaryEntity2.getId());

        mockCreateAccountRequest = new CreateAccountRequest("3333", beneficiaryId2);

        mockOperationsAccountRequest = new OperationsAccountRequest(mockAccountEntity1.getPin(), BigDecimal.valueOf(15));

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
        mappedAccountEntity.setPin(mockCreateAccountRequest.pin());
        mappedAccountEntity.setBalance(BigDecimal.valueOf(0));

        AccountEntity savedAccountEntity = new AccountEntity(
                newAccountId,
                BigDecimal.valueOf(0),
                mockCreateAccountRequest.pin(),
                mockBeneficiaryEntity2
        );

        AccountDto newAccountDto = new AccountDto(
                newAccountId,
                BigDecimal.valueOf(0),
                mockCreateAccountRequest.pin(),
                mockBeneficiaryEntity2.getId()
        );

        when(beneficiaryRepository.findById(mockCreateAccountRequest.beneficiaryId())).thenReturn(Optional.of(mockBeneficiaryEntity2));
        when(accountMapper.dtoToEntity(mockCreateAccountRequest)).thenReturn(mappedAccountEntity);
        when(accountRepository.save(mappedAccountEntity)).thenReturn(savedAccountEntity);
        when(accountMapper.entityToDto(savedAccountEntity)).thenReturn(newAccountDto);

        AccountDto result = accountService.createAccount(mockCreateAccountRequest);

        assertNotNull(result);
        assertEquals(newAccountDto, result);
    }

    @Test
    public void whenCreateNewAccountWithNotExistingBeneficiaryId_thenEntityNotFoundExceptionReturned(){
        when(beneficiaryRepository.findById(notExistingBeneficiaryId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.createAccount(new CreateAccountRequest("3333", notExistingBeneficiaryId)));

        assertEquals("Клиента с id " + notExistingBeneficiaryId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenDepositAccountSuccessfully_thenAccountReturned(){
        mockAccountEntity1.setBalance(mockAccountEntity1.getBalance().add(mockOperationsAccountRequest.amount()));
        AccountDto savedAccountDto1 = new AccountDto(
                mockAccountEntity1.getId(),
                mockAccountEntity1.getBalance(),
                mockAccountEntity1.getPin(),
                mockAccountEntity1.getBeneficiary().getId()
        );

        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.save(mockAccountEntity1)).thenReturn(mockAccountEntity1);
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(savedAccountDto1);

        AccountDto result = accountService.depositAccount(mockAccountEntity1.getId(), mockOperationsAccountRequest);

        assertNotNull(result);
        assertEquals(savedAccountDto1, result);
    }

    @Test
    public void whenDepositNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.depositAccount(notExistingAccountId, mockOperationsAccountRequest));

        assertEquals("Счета с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenDepositAccountWithWrongPin_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));

        InvalidPinException thrown =  assertThrows(InvalidPinException.class,
                () -> accountService.depositAccount(mockAccountEntity1.getId(), new OperationsAccountRequest("1244", BigDecimal.valueOf(111))));

        assertEquals("Неверный pin. Доступ отказан.", thrown.getMessage());
    }

    @Test
    public void whenWithdrawFromAccountSuccessfully_thenAccountReturned(){
        mockAccountEntity1.setBalance(mockAccountEntity1.getBalance().subtract(mockOperationsAccountRequest.amount()));
        AccountDto savedAccountDto1 = new AccountDto(
                mockAccountEntity1.getId(),
                mockAccountEntity1.getBalance(),
                mockAccountEntity1.getPin(),
                mockAccountEntity1.getBeneficiary().getId()
        );

        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.save(mockAccountEntity1)).thenReturn(mockAccountEntity1);
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(savedAccountDto1);

        AccountDto result = accountService.withdrawFromAccount(mockAccountEntity1.getId(), mockOperationsAccountRequest);

        assertNotNull(result);
        assertEquals(savedAccountDto1, result);
    }

    @Test
    public void whenWithdrawFromNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.withdrawFromAccount(notExistingAccountId, mockOperationsAccountRequest));

        assertEquals("Счета с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenWithdrawFromAccountWithWrongPin_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));

        InvalidPinException thrown =  assertThrows(InvalidPinException.class,
                () -> accountService.withdrawFromAccount(mockAccountEntity1.getId(), new OperationsAccountRequest("1244", BigDecimal.valueOf(111))));

        assertEquals("Неверный pin. Доступ отказан.", thrown.getMessage());
    }

    @Test
    public void whenWithdrawFromAccountWithBiggerAmount_thenIllegalArgumentExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));

        IllegalArgumentException thrown =  assertThrows(IllegalArgumentException.class,
                () -> accountService.withdrawFromAccount(mockAccountEntity1.getId(), new OperationsAccountRequest(mockAccountEntity1.getPin(),
                                                                                                                    BigDecimal.valueOf(555555L))));

        assertEquals("Недостаточно средств для вывода.", thrown.getMessage());
    }

    @Test
    public void whenDepositFromAccountToAccountSuccessfully_thenAccountsReturned(){
        mockAccountEntity1.setBalance(mockAccountEntity1.getBalance().subtract(mockOperationsAccountRequest.amount()));
        mockAccountEntity3.setBalance(mockAccountEntity3.getBalance().add(mockOperationsAccountRequest.amount()));
        AccountDto savedAccountDto1 = new AccountDto(
                mockAccountEntity1.getId(),
                mockAccountEntity1.getBalance(),
                mockAccountEntity1.getPin(),
                mockAccountEntity1.getBeneficiary().getId()
        );
        AccountDto savedAccountDto2 = new AccountDto(
                mockAccountEntity3.getId(),
                mockAccountEntity3.getBalance(),
                mockAccountEntity3.getPin(),
                mockAccountEntity3.getBeneficiary().getId()
        );

        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(mockAccountEntity3.getId())).thenReturn(Optional.of(mockAccountEntity3));
        when(accountRepository.save(mockAccountEntity1)).thenReturn(mockAccountEntity1);
        when(accountRepository.save(mockAccountEntity3)).thenReturn(mockAccountEntity3);
        when(accountMapper.entityToDto(mockAccountEntity1)).thenReturn(savedAccountDto1);
        when(accountMapper.entityToDto(mockAccountEntity3)).thenReturn(savedAccountDto2);

        List<AccountDto> result = accountService.transferFromAccountToAccount(mockAccountEntity1.getId(),
                                                                                mockAccountEntity3.getId(),
                                                                                mockOperationsAccountRequest);

        assertNotNull(result);
        assertEquals(List.of(savedAccountDto1, savedAccountDto2), result);
    }

    @Test
    public void whenTransferFromNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.transferFromAccountToAccount(notExistingAccountId,
                                                                    mockAccountEntity3.getId(),
                                                                    mockOperationsAccountRequest));

        assertEquals("Счета отправителя с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenTransferToNotExistingAccount_thenEntityNotFoundExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(notExistingAccountId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown =  assertThrows(EntityNotFoundException.class,
                () -> accountService.transferFromAccountToAccount(mockAccountEntity1.getId(),
                                                                    notExistingAccountId,
                                                                    mockOperationsAccountRequest));

        assertEquals("Счета получателя с id " + notExistingAccountId + " нет.", thrown.getMessage());
    }

    @Test
    public void whenTransferFromAccountToAccountWithWrongPin_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(mockAccountEntity3.getId())).thenReturn(Optional.of(mockAccountEntity3));

        InvalidPinException thrown =  assertThrows(InvalidPinException.class,
                () -> accountService.transferFromAccountToAccount(mockAccountEntity1.getId(),
                                                            mockAccountEntity3.getId(),
                                                            new OperationsAccountRequest("2221", BigDecimal.valueOf(2L))));

        assertEquals("Неверный pin. Доступ отказан.", thrown.getMessage());
    }

    @Test
    public void whenTransferFromAccountToAccountWithBiggerAmount_thenInvalidPinExceptionReturned(){
        when(accountRepository.findById(mockAccountEntity1.getId())).thenReturn(Optional.of(mockAccountEntity1));
        when(accountRepository.findById(mockAccountEntity3.getId())).thenReturn(Optional.of(mockAccountEntity3));

        IllegalArgumentException thrown =  assertThrows(IllegalArgumentException.class,
                () -> accountService.transferFromAccountToAccount(mockAccountEntity1.getId(),
                                                            mockAccountEntity3.getId(),
                                                            new OperationsAccountRequest(mockAccountEntity1.getPin(), BigDecimal.valueOf(555555L))));

        assertEquals("Недостаточно средств для перевода.", thrown.getMessage());
    }
}
