package com.divary.cimbtestrequirement.model;

import com.divary.cimbtestrequirement.audit.AuditEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "transaction_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistory extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionHistoryId;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties(value = {"transactionHistories", "role"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "transaction_type_id", nullable = false)
    @JsonIgnoreProperties(value = {"passwordHash"})
    private TransactionType transactionType;
}
