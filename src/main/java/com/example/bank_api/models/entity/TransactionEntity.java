package com.example.bank_api.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "transactions")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {
    @Id
    @Column(name = "transaction_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_id_sequence", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_from_id", nullable = false)
    private AccountEntity accountFrom;

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
}

