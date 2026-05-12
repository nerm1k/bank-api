package com.example.bank_api.services;

import com.example.bank_api.exceptions.InvalidPinException;
import com.example.bank_api.models.dto.request.CreateAccountRequest;
import com.example.bank_api.models.dto.request.OperationsAccountRequest;
import com.example.bank_api.models.dto.response.AccountDto;
import com.example.bank_api.models.entity.AccountEntity;
import com.example.bank_api.models.entity.BeneficiaryEntity;
import com.example.bank_api.models.entity.TransactionEntity;
import com.example.bank_api.models.entity.TransactionType;
import com.example.bank_api.models.mappers.AccountMapper;
import com.example.bank_api.repositories.AccountRepository;
import com.example.bank_api.repositories.BeneficiaryRepository;
import com.example.bank_api.repositories.TransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    private final TransactionRepository transactionRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository,
                          BeneficiaryRepository beneficiaryRepository,
                          TransactionRepository transactionRepository,
                          AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.beneficiaryRepository = beneficiaryRepository;
        this.transactionRepository = transactionRepository;
        this.accountMapper = accountMapper;
    }

    public List<AccountDto> findAllAccounts() {
        List<AccountEntity> accountEntityList = this.accountRepository.findAll();

        return accountEntityList.stream()
                .sorted(Comparator.comparing(AccountEntity::getId))
                .map(accountMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public AccountDto findAccountById(Long accountId) {
        AccountEntity accountEntity = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Счета с id " + accountId + " нет."));

        return accountMapper.entityToDto(accountEntity);
    }

    public AccountDto createAccount(CreateAccountRequest accountToCreate) {
        BeneficiaryEntity beneficiaryEntity = this.beneficiaryRepository.findById(accountToCreate.beneficiaryId())
                .orElseThrow(() -> new EntityNotFoundException("Клиента с id " + accountToCreate.beneficiaryId() + " нет."));

        AccountEntity accountEntity = accountMapper.dtoToEntity(accountToCreate);
        accountEntity.setBeneficiary(beneficiaryEntity);
        AccountEntity savedAccountEntity = this.accountRepository.save(accountEntity);

        return accountMapper.entityToDto(savedAccountEntity);
    }

    public AccountDto depositAccount(Long accountId, OperationsAccountRequest depositAccountRequest) {
        AccountEntity accountEntity = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Счета с id " + accountId + " нет."));

        if (!accountEntity.getPin().equals(depositAccountRequest.pin())){
            throw new InvalidPinException("Неверный pin. Доступ отказан.");
        }

        accountEntity.setBalance(accountEntity.getBalance().add(depositAccountRequest.amount()));
        AccountEntity savedAccountEntity = this.accountRepository.save(accountEntity);

        transactionRepository.save(new TransactionEntity(
                savedAccountEntity,
                depositAccountRequest.amount(),
                TransactionType.DEPOSIT
        ));

        return accountMapper.entityToDto(savedAccountEntity);
    }

    public AccountDto withdrawFromAccount(Long accountId, OperationsAccountRequest withdrawAccountRequest) {
        AccountEntity accountEntity = this.accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException("Счета с id " + accountId + " нет."));

        if (!accountEntity.getPin().equals(withdrawAccountRequest.pin())){
            throw new InvalidPinException("Неверный pin. Доступ отказан.");
        }

        BigDecimal newBalance = accountEntity.getBalance().subtract(withdrawAccountRequest.amount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Недостаточно средств для вывода.");
        }

        accountEntity.setBalance(accountEntity.getBalance().subtract(withdrawAccountRequest.amount()));
        AccountEntity savedAccountEntity = this.accountRepository.save(accountEntity);

        transactionRepository.save(new TransactionEntity(
                savedAccountEntity,
                withdrawAccountRequest.amount().negate(),
                TransactionType.WITHDRAW
        ));

        return accountMapper.entityToDto(savedAccountEntity);
    }

    @Transactional
    public List<AccountDto> transferFromAccountToAccount(Long accountFromId, Long accountToId, OperationsAccountRequest transferAccountRequest) {
        AccountEntity accountFromEntity = this.accountRepository.findById(accountFromId)
                .orElseThrow(() -> new EntityNotFoundException("Счета отправителя с id " + accountFromId + " нет."));

        AccountEntity accountToEntity = this.accountRepository.findById(accountToId)
                .orElseThrow(() -> new EntityNotFoundException("Счета получателя с id " + accountToId + " нет."));

        if (!accountFromEntity.getPin().equals(transferAccountRequest.pin())){
            throw new InvalidPinException("Неверный pin. Доступ отказан.");
        }

        BigDecimal newBalance = accountFromEntity.getBalance().subtract(transferAccountRequest.amount());
        if (newBalance.compareTo(BigDecimal.ZERO) < 0){
            throw new IllegalArgumentException("Недостаточно средств для перевода.");
        }

        accountFromEntity.setBalance(accountFromEntity.getBalance().subtract(transferAccountRequest.amount()));
        AccountEntity savedAccountFromEntity = this.accountRepository.save(accountFromEntity);

        accountToEntity.setBalance(accountToEntity.getBalance().add(transferAccountRequest.amount()));
        AccountEntity savedAccountToEntity = this.accountRepository.save(accountToEntity);

        transactionRepository.save(new TransactionEntity(
                savedAccountFromEntity,
                savedAccountToEntity,
                transferAccountRequest.amount().negate(),
                TransactionType.TRANSFER
        ));

        return Stream.of(savedAccountFromEntity, savedAccountToEntity)
                .map(accountMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
