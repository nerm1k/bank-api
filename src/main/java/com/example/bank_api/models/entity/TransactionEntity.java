package com.example.bank_api.models.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "transactions")
public class TransactionEntity {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_id_sequence", allocationSize = 1)
    private Long id;

//    @Column(name = "account_from_id", nullable = false)
//    private Long accountFromId;

    @ManyToOne
    @JoinColumn(name = "account_from_id", nullable = false)
    private AccountEntity accountFrom;

//    @Column(name = "account_to_id", nullable = true)
//    private Long accountToId;

    @ManyToOne
    @JoinColumn(name = "account_to_id", nullable = true)
    private AccountEntity accountTo;

    @Column(name = "balance_change", nullable = false, precision = 19, scale = 2)
    private BigDecimal balanceChange;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    public TransactionEntity(){

    }

    public TransactionEntity(AccountEntity accountFrom,
                             BigDecimal balanceChange,
                             TransactionType transactionType)
    {
        this.accountFrom = accountFrom;
        this.balanceChange = balanceChange;
        this.transactionType = transactionType;
    }

    public TransactionEntity(AccountEntity accountFrom,
                             AccountEntity accountTo,
                             BigDecimal balanceChange,
                             TransactionType transactionType)
    {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.balanceChange = balanceChange;
        this.transactionType = transactionType;
    }

    public TransactionEntity(Long id,
//                             Long accountFromId,
                             AccountEntity accountFrom,
//                             Long accountToId,
                             AccountEntity accountTo,
                             BigDecimal balanceChange,
                             TransactionType transactionType,
                             LocalDateTime createdAt)
    {
        this.id = id;
//        this.accountFromId = accountFromId;
        this.accountFrom = accountFrom;
//        this.accountToId = accountToId;
        this.accountTo = accountTo;
        this.balanceChange = balanceChange;
        this.transactionType = transactionType;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Long getAccountFromId() {
//        return accountFromId;
//    }
//
//    public void setAccountFromId(Long accountFromId) {
//        this.accountFromId = accountFromId;
//    }

    public AccountEntity getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(AccountEntity accountFrom) {
        this.accountFrom = accountFrom;
    }

//    public Long getAccountToId() {
//        return accountToId;
//    }
//
//    public void setAccountToId(Long accountToId) {
//        this.accountToId = accountToId;
//    }

    public AccountEntity getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(AccountEntity accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getBalanceChange() {
        return balanceChange;
    }

    public void setBalanceChange(BigDecimal balanceChange) {
        this.balanceChange = balanceChange;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

