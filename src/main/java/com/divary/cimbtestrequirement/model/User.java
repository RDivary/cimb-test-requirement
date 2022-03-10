package com.divary.cimbtestrequirement.model;

import com.divary.cimbtestrequirement.enums.RolesEnum;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    @Column(length = 100, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String passwordHash;

    @Column(length = 100, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private RolesEnum role;
}
