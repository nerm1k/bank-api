package com.example.bank_api.models.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "beneficiaries")
public class BeneficiaryEntity {
    @Id
    @Column(name = "beneficiary_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ben_seq")
    @SequenceGenerator(name = "ben_seq", sequenceName = "beneficiary_id_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "beneficiary")
    private List<AccountEntity> accounts;

    public BeneficiaryEntity(){

    }

    public BeneficiaryEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AccountEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountEntity> accounts) {
        this.accounts = accounts;
    }
}
