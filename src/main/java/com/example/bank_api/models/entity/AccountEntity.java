package com.example.bank_api.models.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class AccountEntity {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acc_seq")
    @SequenceGenerator(name = "acc_seq", sequenceName = "account_id_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "pin", nullable = false, length = 4)
    private String pin;

//    @Column(name = "beneficiary_id", nullable = false)
//    private Long beneficiaryId;

    @ManyToOne
    @JoinColumn(name = "beneficiary_id", nullable = false)
    private BeneficiaryEntity beneficiary;

    public AccountEntity() {
    }

    public AccountEntity(Long id, BigDecimal balance, String pin, BeneficiaryEntity beneficiary) {
        this.id = id;
        this.balance = balance;
        this.pin = pin;
//        this.beneficiaryId = beneficiaryId;
        this.beneficiary = beneficiary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

//    public Long getBeneficiaryId() {
//        return beneficiaryId;
//    }
//
//    public void setBeneficiaryId(Long beneficiaryId) {
//        this.beneficiaryId = beneficiaryId;
//    }

    public BeneficiaryEntity getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary (BeneficiaryEntity beneficiary) {
        this.beneficiary = beneficiary;
    }
}
