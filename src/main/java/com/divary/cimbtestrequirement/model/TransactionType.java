package com.divary.cimbtestrequirement.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "transaction_type")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long transactionTypeId;

    @Column(length = 50, unique = true, nullable = false)
    private String transactionCode;

    @Column(nullable = false)
    private String transactionName;

    private boolean active;
}
