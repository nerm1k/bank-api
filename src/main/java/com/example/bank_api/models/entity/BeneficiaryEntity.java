package com.example.bank_api.models.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "beneficiaries")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
